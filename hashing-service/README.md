./mvnw spring-boot:run

POST http://localhost:8080/api/service
Content-Type
application/json
Body
{
  "id": "job-1",
  "lines": [
    "This a line",
    "And that's another line."
  ]
}

Output:
{
"id": "job-1",
"lines": [
  "-681809931",
  "1874030041"
],
}