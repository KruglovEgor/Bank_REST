<h1>🚀 Система управления банковскими картами</h1>

Код и инструкции запуска. Подробности функционала и эндпоинтов см. в <code>README_Bank_rest.md</code>.

<h2>▶️ Быстрый старт (Docker, без Maven локально)</h2>
<ol>
  <li>Запустить БД (PostgreSQL):
    <pre><code>docker compose up -d db</code></pre>
  </li>
  <li>Собрать и запустить приложение:
    <pre><code>docker compose up --build</code></pre>
  </li>
  <li>Открыть Swagger UI: <code>http://localhost:8080/swagger-ui.html</code></li>
</ol>

<h3>Полезные переменные окружения</h3>
<ul>
  <li><code>DB_HOST</code>, <code>DB_PORT</code>, <code>DB_NAME</code>, <code>DB_USER</code>, <code>DB_PASSWORD</code></li>
  <li><code>JWT_SECRET</code>, <code>JWT_EXP_MIN</code></li>
  <li><code>CARD_SECRET</code></li>
</ul>

<h2>▶️ Локально (если Maven установлен)</h2>
<ol>
  <li>Поднять БД:
    <pre><code>docker compose up -d db</code></pre>
  </li>
  <li>Запустить приложение:
    <pre><code>mvn spring-boot:run</code></pre>
  </li>
  <li>Либо собрать и запустить jar:
    <pre><code>mvn -DskipTests package
java -jar target/bankcards-0.0.1-SNAPSHOT.jar</code></pre>
  </li>
</ol>

<h2>🔗 Быстрые ссылки</h2>
<ul>
  <li>Swagger UI: <code>http://localhost:8080/swagger-ui.html</code></li>
  <li>API docs (JSON): <code>http://localhost:8080/v3/api-docs</code></li>
</ul>
