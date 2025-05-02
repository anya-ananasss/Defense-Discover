import os

import requests
import json
import re
from dotenv import load_dotenv

difficulty_levels = {
    "легкий": "простой вопрос",
    "средний": "интересный вопрос с умеренной сложностью",
    "сложный": "сложный и нестандартный вопрос"
}

TOKEN = os.getenv('TOKEN')

QWEN_URL = "https://openrouter.ai/api/v1/chat/completions"
QWEN_HEADERS = {
    "Authorization": f"Bearer {TOKEN}",
    "Content-Type": "application/json",
}


def generate_quiz_question_with_choices_json(topic, num_questions=5, difficulty="средний"):
    if difficulty not in difficulty_levels:
        raise ValueError("Допустимые уровни сложности: 'легкий', 'средний', 'сложный'.")

    messages = [
        {"role": "user",
         "content": f"Придумай {num_questions} {difficulty} неповторяющихся вопроса для викторины по теме {topic}. Выведи результат в виде JSON с полями: question (вопрос), options (4 варианта ответов) и correct_answer (правильный ответ).",
         "extra": {}, "chat_type": "t2t"},
    ]

    payload = {
        "chat_type": "t2t",
        "messages": messages,
        "model": "qwen/qwen2.5-vl-72b-instruct:free",
        "stream": False
    }

    response = requests.post(QWEN_URL, headers=QWEN_HEADERS, json=payload)

    if response.status_code == 200:
        try:
            result = response.json()

            # Проверяем, есть ли нужное поле в ответе
            if "choices" in result and result["choices"] and "message" in result["choices"][0]:
                quiz_data = result["choices"][0]["message"]["content"].strip()

                # Ищем содержимое между '''json и '''
                match = re.search(r"```json\s*(.*?)\s*```", quiz_data, re.DOTALL)

                if match:
                    json_content = match.group(1).strip()  # Извлекаем JSON
                    quiz_json = json.loads(json_content)  # Преобразуем в объект Python

                    # Записываем в файл
                    with open("quiz_questions.json", "w", encoding="utf-8") as file:
                        json.dump(quiz_json, file, ensure_ascii=False, indent=4)

                    print("JSON успешно записан в 'quiz_questions.json'")
                else:
                    print("Ошибка: JSON-данные внутри '''json ... ''' не найдены!")

            else:
                print("Ошибка: Нужное поле в ответе отсутствует!")

        except json.JSONDecodeError as e:
            print("Ошибка декодирования JSON:", e)

    else:
        print(f"Ошибка при отправке запроса: {response.status_code}")
        print("Текст ошибки:", response.text)


generate_quiz_question_with_choices_json("Музыка", num_questions=3, difficulty="легкий")
