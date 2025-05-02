from dotenv import load_dotenv
import os

load_dotenv(os.path.join(os.path.dirname(__file__), "../private/token.env"))


class Config:
    QWEN_API_TOKEN = os.getenv('TOKEN')
    QWEN_API_URL = "https://openrouter.ai/api/v1/chat/completions"
    QWEN_MODEL_NAME = "qwen/qwen2.5-vl-72b-instruct:free"
