# Movies App
### Overview
Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, I built an app to allow users to discover the most popular movies playing.
### Features
* Present users with a grid arrangement of movie posters upon launch.
* Allow users to change sort order via a setting:
  - Popular Movies
  - Top Rated Movies
  - Favourites
* Allow users to tap on a movie poster and transition to a details screen with additional information such as:
  - original title
  - movie poster
  - A plot synopsis
  - user rating 
  - release date
* Allow users to view and play trailers ( either in the youtube app or a web browser).
* Allow users to read reviews of a selected movie.
* Allow users to mark a movie as a favorite in the details view.
* App uses content provider to store names, ids and the rest of the information needed to display their favorites collection while offline.
### Screenshots
 ![ ](screenshots/1.png?raw=true )
 ![ ](screenshots/2.png?raw=true )
 ![ ](screenshots/3.png?raw=true )
 ![ ](screenshots/4.png?raw=true )
 ![ ](screenshots/5.png?raw=true )
 ![ ](screenshots/6.png?raw=true )
 ![ ](screenshots/7.png?raw=true )
 ![ ](screenshots/8.png?raw=true )

### Building Instructions
The app uses themoviedb.org API to fetch movies data.
If you don’t already have an account, you will need to [create one](https://www.themoviedb.org/account/signup "create account here") in order to request an API Key.
When you have your API_KEY go to NetworkUtilities.java file, edit line number 22 and put your API_KEY and it's done and now you can run the project :)
