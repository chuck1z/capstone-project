# capstone-project - FRESHCAM (MD path - android kotlin)
![FreshCam!](.././fontart.png?raw=true "ArtFont") 

## About
![ezgif com-gif-maker (2)](https://user-images.githubusercontent.com/65596335/173263743-c529bb47-a025-46ec-83a8-3a99767c27df.gif)
<image src="[https://giphy.com/embed/facKDNMJo43ubb530s](https://user-images.githubusercontent.com/65596335/173263743-c529bb47-a025-46ec-83a8-3a99767c27df.gif)" width="200" />
<img src="https://s8.gifyu.com/images/demo3.gif" width="200" />
this app using :

- kotlinx - flow
- jetpack navigation
- single activity style
- android architecture component
- dagger-hilt
- CameraX
- Retrofit
- room database
- datastore
- firebase (analytics,crashlitycs,remote config, customl)

## project structure

from main/java

* app
    * App.kt
    * Config.kt (app configuration)
    * DI (Dependencies Injection modules)
* data (data layer of app)
    * networks
    * datasources
    * models
    * repositories
* presentation (Presentation layer of app)
    * Feature A
        * views
        * viewmodel
    * Feature B
        * views
        * viewmodel
* utils (utility class or function)


## App Sneakpeak


## how to deploy this app

- clone this repository ```https://github.com/chuck1z/capstone-project.git``` or you can download this folder only on [download MD folder](https://minhaskamal.github.io/DownGit/#/home?url=https://github.com/chuck1z/capstone-project/tree/main/MD)
- open this project in you android, wait until gradle build done.
- configure your project to connect with your firebase account, follow this step:
  - click ``Tools`` on your toolbar, select ``firebase`` to open firebase assistant menu 
  - <img width="200" alt="Screen Shot 2022-06-12 at 14 20 52" src="https://user-images.githubusercontent.com/65596335/173222152-746ba672-9cf5-4cde-a396-dbef4f88c47c.png">
  - select remote config and then select setup firebase remote config and follow the step until firebase connected and then left.
  ** ``note : make sure the cloud computing project department has prepared the project in your firebase console, both remote config and customl.``
  -  <img width="200" alt="Screen Shot 2022-06-12 at 16 39 08" src="https://user-images.githubusercontent.com/65596335/173227211-a4bce1b8-734d-4c36-9db3-1a0bc3069150.png">
- after configuring your app to connect to firebase, you can build your app into apk file or you can generate appbundle.
