FROM python:3.11-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл зависимостей в контейнер
COPY requirements.txt /app/

# Устанавливаем зависимости
RUN pip install --no-cache-dir -r requirements.txt

# Копируем весь проект в контейнер
COPY . /app/

COPY ModelGateway/private/token.env ModelGateway/private/token.env

# Открываем порт, на котором будет работать FastAPI
EXPOSE 8000

# Указываем команду для запуска FastAPI через Uvicorn
CMD ["uvicorn", "ModelGateway.utils.quiz_api:app", "--host", "0.0.0.0", "--port", "8000"]