<p align="center">
    <a href="https://gitlab.stud.idi.ntnu.no/idatt2106-gruppe-2/idatt2106_2024_02_backend" target="_blank">
        <img width="40%" src="https://cdn.discordapp.com/attachments/1229758495767658566/1235997795555475529/image.png?ex=66366826&is=663516a6&hm=45dc1766b8463011e2a354bbfe837cd8da5f9f8150cc68fde30f66cc5230c252&">
    </a>
    </p>
<h1 align="center">
                Sparesti backend 
</h1>

## 🚀 Getting started
### Requirements
To run the application, you need the following installed: 
- Java 17
- Maven
- Make
- Docker
- Git

### ⚙ Setup
#### The following setup is mandatory before running the project
The application is dependent on certain environment variables in order to run.

Add the following .env file to the root directory after cloning the project:
```
# Insert the following .env file to root directory after cloning

DB_NAME=sparesti_db
MYSQL_URL=jdbc:mysql://db:3306/${DB_NAME}
MYSQL_USERNAME=root
MYSQL_ROOT_PASSWORD=password

SECRET_KEY=87EF7185F1C9A5053F5D0B25CCDF039FA3DEF82C0504ACDC9110FC444237BC57

EMAIL_USERNAME=sparestigruppe2@gmail.com
EMAIL_PASSWORD=wakd klwc nivw whzl
```

### 🚗 Running the application
#### The application can be run with one command in the following joint repository: [Joint-Repository](https://gitlab.stud.idi.ntnu.no/idatt2106-gruppe-2/idatt2106_2024_02)

#### If you want to run the backend independently, follow these instructions:

1. Clone the repository
```
git clone git@gitlab.stud.idi.ntnu.no:idatt2106-gruppe-2/idatt2106_2024_02_backend.git
```
2. Navigate to the project root folder
```
cd idatt2106_2024_02_backend
```
3. Create an ```.env``` file in the project root directory by copying over the ```.env.example``` file: 
```
cp .env.example .env
```
If you are on Windows Command Prompt use: 
```
copy .env.example .env
```

> **_NOTE_**: We are fully aware that this file should not be public and that the user should 
use their own configuration parameters, but 
for the ease of the examiners we have chosen to do it this way.

4. Run the application (OBS: Make sure docker is running). It will take a few minutes the first time you run the application. 
```
make run
```
The application is now running on port 8080.

> **_NOTE_**: If you don't have Make installed, use the following command instead: 
```
docker compose up
```

##### Exiting the application

To exit the application, use CTRL+C. You can rerun the application by using 
```make run``` again.

### 🧪 Test data
The database is populated with a pre-configured user at runtime which 
can be used to experiment with the application. You can log in with the following credentials:
- Username: ```username```
- Password: ```Test123!```

### 📋 Running tests
You can run unit and integration tests with: 
```
make test
```
This will generate a ```target``` folder with test coverage data. You can now find the report 
by navigating the folder structure: ```target -> site -> jacoco -> index.html```.

### 🗎 Documentation
#### REST endpoints
The REST endpoints are documented using [Swagger UI](https://swagger.io/tools/swagger-ui/). The documentation is generated at runtime and can be found [here](http://localhost:8080/swagger-ui/index.html).

#### Source code
The entire backend source code has been documented with Javadoc. The entire documentation is deployed with Gitlab Pages and can be found [here](https://idatt2106-2024-02-backend-idatt2106-gruppe-2-bae057cdb765bc09e0.pages.stud.idi.ntnu.no).

### ⌨️ Other commands
Run ```make help``` to get a list of all possible commands, along with a short explanation for their use case. Alternatively, take a look at the [Makefile](Makefile) for what each command does programmatically. Some of the commands are also mentioned below: 
- ```make test```: Test the application
- ```make destroy```: Tear down the application
- ```make format```: Format the source code
- ```make build```: Build the containers that the application runs in
- ```make compile```: Compile the source code
