import json

from llm_config.config import Config
from llm_config.constants import difficulty_levels
from llm_config.prompts import quiz_prompt
from llm_config.qwen_llm import QwenLLM
from utils.quiz_json_parser import QuizJSONParser


def generate_quiz(topic: str, num_questions: int = 5, difficulty: str = "средний"):
    llm = QwenLLM(
        api_token=Config.QWEN_API_TOKEN,
        api_url=Config.QWEN_API_URL,
        model_name=Config.QWEN_MODEL_NAME
    )

    chain = (
            {
                "topic": lambda x: x["topic"],
                "num_questions": lambda x: x["num_questions"],
                "difficulty": lambda x: x["difficulty"],
            }
            | quiz_prompt
            | llm
            | QuizJSONParser()
    )

    try:
        inputs = {
            "topic": topic,
            "num_questions": num_questions,
            "difficulty": difficulty_levels.get(difficulty, "средний")
        }
        result = chain.invoke(inputs)

        with open("quiz_questions.json", "w", encoding="utf-8") as f:
            json.dump(result, f, ensure_ascii=False, indent=4)
        print("JSON успешно записан в 'quiz_questions.json'")
    except Exception as e:
        print(f"Ошибка: {str(e)}")


if __name__ == "__main__":
    generate_quiz("Музыка", num_questions=3, difficulty="легкий")
