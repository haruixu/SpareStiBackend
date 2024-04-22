<p align="center">
    <a href="https://gitlab.stud.idi.ntnu.no/idatt2106-gruppe-2/idatt2106_2024_02_backend" target="_blank">
        <img width="30%" src="https://m.media-amazon.com/images/I/51ckv2myWXL.jpg" alt="Sparesti logo">
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

### ⚙ Setup
Something about creating .env file with own configuration for database 
copying over example .env file?

### 🚗 Running the application
1. Clone the repository
```
git clone https://gitlab.stud.idi.ntnu.no/idatt2106-gruppe-2/idatt2106_2024_02_backend
```
2. Navigate to the project root folder
```
cd idatt2106_2024_02_backend
```
3. Build the application (OBS: Make sure docker is running)
```
make build
```
4. Run the application
```
make run
```
The application is now running on port 8080. 

To exit the application, use CTRL+C. You can rerun the application using 
```make run``` again.

### ⌨️ Other commands
Run ```make help``` to get a short explanation for all available commands or take a look at the [Makefile](Makefile) for what each command does programmatically. Some of the commands are also mentioned below: 
- ```make test```: Test the application
- ```make destroy```: Tear down the application
- ```make format```: Format the source code
