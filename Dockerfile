FROM python:3.6.12-alpine
WORKDIR /app
COPY requirement.txt .
COPY main.py .
RUN pip install -r requirement.txt
CMD ["python", "main.py"]