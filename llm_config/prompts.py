from langchain.prompts import PromptTemplate

quiz_prompt = PromptTemplate(
    input_variables=["topic", "num_questions", "difficulty"],
    template="""Придумай {num_questions} {difficulty} неповторяющихся вопроса для викторины по теме {topic}. 
    Выведи результат в виде JSON с полями: question (вопрос), options (4 варианта ответов) и correct_answer (правильный ответ).""",
)
