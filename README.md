<h1>UAA - User Authentication and Authorization service</h1>

<p>
An authentication and authorization microservice to handle user registration, authentication and JWT token validation.
</p>

<h2>Set up environment</h2>

<h3>Steps</h3>
<ol>
<li>Get the source code: git clone https://github.com/cjclementson/app-hub-uaa.git.</li>
<li>Install <a href="https://www.oracle.com/java/technologies/downloads/#java17">JDK 17</a>.</li>
<li>Install <a href="https://maven.apache.org/download.cgi">Maven</a>.</li>
<li>Install <a href="https://www.docker.com/products/docker-desktop/">Docker Desktop</a>.</li>
<li>Make sure maven is on the your path.</li>
</ol>

<h2>Run PostgreSql container</h2>

<h3>Pre requisites</h3>
<ul>
<li>Docker Desktop installed and running.</li>
<li>Docker compose is on your path.</li>
</ul>

<h3>Steps</h3>
<ol>
<li>From within the root directory of your repository, navigate to the docker-compose directory.</li>
<li>Edit the .env file and replace <b>{enter-db-username}</b>, <b>{enter-db-password}</b> and <b>{enter-db-name}</b> with your own values.</li>
<li>Open cmd. From within the root directory of your repository, navigate to the deployment directory.</li>
<li>Windows: run startup-database.bat.</li>
</ol>

<p>The PostgreSql container should be running and the specified database should be created.</p>

<h2>Build and run</h2>

<h3>Pre requisites</h3>
<ul>
<li>Source code downloaded.</li>
<li>Maven is installed and on your path.</li>
<li>PostgreSql contiainer is running.</li>
</ul>

<h3>Steps</h3>
<ol>
<li>Open cmd. From within the root directory of your repository, navigate to the deployment directory.</li>
<li>Windows: run compile.bat. The result should be a docker image com.app.hub.uaa/uaa.</li>
<li>Windows: run startup-services.bat.</li>
</ol>

<h2>Using the API</h2>

<h3>Pre requisites</h3>
<p>Curl or Postman is installed.</p>

<h3>Steps</h3>

<p>See the curl commands below for examples on using the API.</p>

<h4>User registration</h4>
<p>Request</p>

```
curl -d '{"email": "your_email@gmail.com", "username": "your_username", "password": "your_password" }' -H 'Content-Type: application/json' -X POST "http://localhost:8000/api/v1/auth/register"
```

<p>Response</p>

```
{"token": "<generated_token>" }
```

<h4>User login</h4>
<p>Request</p>

```
curl -d '{"email": "your_email@gmail.com", "password": "your_password" }' -H 'Content-Type: application/json' -X POST "http://localhost:8000/api/v1/auth/login"
```

<p>Response</p>

```
{"token": "<generated_token>" }
```

<h4>Token validation</h4>
<p>Request</p>

```
curl -d '{"token": "your_generated_token" }' -H 'Content-Type: application/json' -X POST "http://localhost:8000/api/v1/auth/validate"
```

<p>Response</p>

```
200 HTTP Status code
```
