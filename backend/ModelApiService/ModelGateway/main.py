from backend.ModelApiService.ModelGateway.utils.quiz_api import generate_quiz

if __name__ == "__main__":
    print(generate_quiz("Физика", num_questions=3, difficulty="легкий", key_words=["заряд", "Ньютон", "давление"]))
