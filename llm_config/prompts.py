from langchain.prompts import PromptTemplate

quiz_prompt = PromptTemplate(
    input_variables=["topic", "num_questions", "difficulty", "key_words"],
    template="""Придумай {num_questions} {difficulty} неповторяющихся вопроса для викторины по теме {topic}. Не используй эти слова: {key_words}. 
    Выведи результат в виде JSON с полями: topic (тема), question (вопрос), options (4 варианта ответов), correct_answer (правильный ответ) и key_words (ключевые слова).""",
)
