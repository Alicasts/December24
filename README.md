**This is a travel management app developed as part of a code challenge.
It allows users to request rides, view their history, and manage travel options accordingly to the code challenge requirements.
As part of this project, I utilized my knowledge of agile methodologies to organize the development process effectively. I created a Kanban board, implemented a ticket system, and drafted a basic documentation structure on Trello to manage tasks and workflows efficiently. You can view the Trello board here: (link available after the time deadline)**

![Demo](docs/DemoGif.gif)

 **Table of Contents**
* Features
* Technologies Used
* Environment Setup
* How to Run
* Project Structure
* Final Notes

**Features**
* Request rides with origin and destination.
* View travel history.
* Select customized options for drivers and vehicles.
* Map integration for routes and destinations.

**Technologies Used**
* Language: Kotlin
* Frameworks/Architecture: MVVM, Jetpack Compose
* Libraries:
   *    Retrofit (for API calls)
   *    Hilt (for dependency injection)
   *    Mapbox (for maps and routes)
* Testing: JUnit4, Espresso
* Others: Gradle for dependency management

**Environment Setup**
* Clone the repository: https://github.com/Alicasts/December24.git
* Set up the Android environment:
   * Make sure Android Studio is installed.
   * Configure the Android SDK.
   * Use Java 11.
* Add the Mapbox key (if applicable): Replace MAPBOX_API_KEY in the utils.Secret file for a valid one.

**Project Structure**

app/  
├── data/                # Data layer (Remote and Local)  
├── di/                  # Dependency Injection (AppModule)  
├── presentation/        # Screens, ViewModels, and components  
├── utils/               # Utility functions and classes

**Final Notes and Personal Considerations**

* Since I'm not experienced with Jetpack Compose UI yet, I only developed the basics and did not include Instrumented Tests.
* This project was developed as part of a code challenge.
* Feedback and suggestions are welcome!
