import json

from ModelGateway.test_db.db_utils import get_or_create_category, add_keywords, connect_to_db


# Основной скрипт
def main(json_file_path):
    # Чтение JSON из файла
    try:
        with open(json_file_path, 'r', encoding='utf-8') as file:
            data = json.load(file)
    except FileNotFoundError:
        print(f"Ошибка: Файл {json_file_path} не найден.")
        return
    except json.JSONDecodeError:
        print(f"Ошибка: Неверный формат JSON в файле {json_file_path}.")
        return

    # Подключение к базе данных и обработка данных
    conn = connect_to_db()
    try:
        with conn.cursor() as cursor:
            for item in data:
                # Получаем или создаем категорию
                category_id = get_or_create_category(cursor, item["topic"])

                # Добавляем ключевые слова
                add_keywords(cursor, category_id, item["key_words"])

            # Фиксируем изменения
            conn.commit()
    except Exception as e:
        print(f"Ошибка при работе с базой данных: {e}")
        conn.rollback()
    finally:
        conn.close()


if __name__ == "__main__":
    json_file_path = "quiz_questions.json"
    main(json_file_path)
