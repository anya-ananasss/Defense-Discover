from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

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
