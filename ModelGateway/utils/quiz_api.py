from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import json

from ModelGateway.utils.generation import generate_quiz

app = FastAPI()


class QuizRequest(BaseModel):
    topic: str = "Музыка"
    num_questions: int = 5
    difficulty: str = "средний"
    key_words: list[str] = []


@app.post("/generate_quiz")
async def api_generate_quiz(request: QuizRequest):
    try:
        generate_quiz(
            topic=request.topic,
            num_questions=request.num_questions,
            difficulty=request.difficulty,
            key_words=request.key_words
        )

        with open("./quiz_questions.json", "r", encoding="utf-8") as f:
            result = json.load(f)

        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)
