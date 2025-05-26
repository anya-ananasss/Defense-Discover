from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from ..llm_config.config import Config
from ..llm_config.constants import difficulty_levels
from ..llm_config.prompts import quiz_prompt
from ..llm_config.qwen_llm import QwenLLM
from ..utils.quiz_json_parser import QuizJSONParser


app = FastAPI()


class QuizRequest(BaseModel):
    topic: str = "Музыка"
    num_questions: int = 5
    difficulty: str = "средний"
    key_words: list[str] = []

def generate_quiz(topic: str, num_questions: int = 5, difficulty: str = "средний", key_words=None):
    if key_words is None:
        key_words = []

    llm = QwenLLM()

    chain = (
            {
                "topic": lambda x: x["topic"],
                "num_questions": lambda x: x["num_questions"],
                "difficulty": lambda x: x["difficulty"],
                "key_words": lambda x: x["key_words"],
            }
            | quiz_prompt
            | llm
            | QuizJSONParser()
    )

    for attempt in range(1, 101):
        try:
            inputs = {
                "topic": topic,
                "num_questions": num_questions,
                "difficulty": difficulty_levels.get(difficulty, "средний"),
                "key_words": key_words
            }
            result = chain.invoke(inputs)
            return result

        except Exception as e:
            print(f"Попытка {attempt}/{100}: Ошибка - {str(e)}")
            if attempt == 100:
                raise Exception("Не удалось сгенерировать викторину после 100 попыток.")


@app.post("/generate_quiz")
async def api_generate_quiz(request: QuizRequest):
    try:
        result = generate_quiz(
            topic=request.topic,
            num_questions=request.num_questions,
            difficulty=request.difficulty,
            key_words=request.key_words
        )

        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)
