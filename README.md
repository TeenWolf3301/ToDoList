# ToDoList

A simple and minimal To-Do list app built in Kotlin.

## Features

 - Create/Edit/Delete task
 - Category for each task
 - Priority for each task
 - Sort by Name/Date Created/Priority
 - Hide all completed task

## Used libs and technologies

 - Room
 - DaggerHilt
 - ViewModel
   * LiveData
 - RecyclerView
   * RecyclerView.ItemDecoration
 - Kotlin Coroutines
   * Coroutines FLow
 - DataStore preferences
 - NavigationComponent
 - Material Components

### MainScreen (ListFragment)

DarkTheme | LightTheme
--- | ---
![DarkThemeEmpty](https://user-images.githubusercontent.com/32799066/121696868-2d319100-cad5-11eb-82a1-40347596cf9b.jpg) | ![LightThemeEmpty](https://user-images.githubusercontent.com/32799066/121696907-36baf900-cad5-11eb-8b7c-a5decb674689.jpg)
![DarkThemeMainList](https://user-images.githubusercontent.com/32799066/121696941-3de20700-cad5-11eb-8bec-002b3e0d8a1f.jpg) | ![LightThemeMainList](https://user-images.githubusercontent.com/32799066/121696950-3f133400-cad5-11eb-8e72-5fb9b0558135.jpg)

### DetailsFragment
 
DarkTheme | LightTheme
--- | ---
![DarkThemeDetailsFragment](https://user-images.githubusercontent.com/32799066/121697165-7550b380-cad5-11eb-8a9d-f8d738ebdad3.jpg) | ![LightThemeDetailsFragment](https://user-images.githubusercontent.com/32799066/121697432-bc3ea900-cad5-11eb-8478-6dadbba6d9c8.jpg)
