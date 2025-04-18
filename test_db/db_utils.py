import psycopg2


def connect_to_db():
    return psycopg2.connect(
        dbname="D&D",
        user="postgres",
        password="1234",
        host="localhost",
        port="5432"
    )


SQL_GET_OR_CREATE_CATEGORY = """
    SELECT id FROM Category WHERE name = %s
"""

SQL_INSERT_CATEGORY = """
    INSERT INTO Category (name) VALUES (%s) RETURNING id
"""

SQL_INSERT_KEYWORDS = """
    INSERT INTO KeyWords (category_id, words) VALUES (%s, %s)
"""


def get_or_create_category(cursor, topic):
    cursor.execute(SQL_GET_OR_CREATE_CATEGORY, (topic,))
    result = cursor.fetchone()
    if result:
        return result[0]
    else:
        cursor.execute(SQL_INSERT_CATEGORY, (topic,))
        return cursor.fetchone()[0]


def add_keywords(cursor, category_id, key_words):
    words_str = ",".join(word.lower() for word in key_words)
    cursor.execute(SQL_INSERT_KEYWORDS, (category_id, words_str))
