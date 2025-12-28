# Anime-Suggest-App

### Home Screen Activity
* Fetches Anime List from API and shown by using Recyclerview and each item designed within Material CardView.
* Shows Image poster of the Anime and Title, Number of Episodes, Rating of Anime.
* Used Glide Library to load Image Posters from the URL.
* Onclick of An Anime Item will navigate to Detail Anime Activity

### Detail Anime Activity
* Fetches Details of the Anime by using mal_id.
* Details of Anime include Title, Rating, Number of Episodes, Duration, Genres, Main Cast, Synopsis.
* Used Android Youtube Player Library to Play the trailer of Anime (which is an youtube video link).
* For Main Cast, Need to fetch Characters which isn't available in ```anime\{id}```. I went through Documentaion of Jikan API, found that it is supporting ```anime\{id}\charachters``` API which will return the characters data for an Anime. With this one I'm Fetching and showing Main Cast for an Anime.

### Release Build
* If you want to test this App, You can Find Release build for this project in release folder. 
