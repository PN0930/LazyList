package com.example.lazylist.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel


data class Joke(val setup: String, val punchline: String)

class ViewModel : ViewModel() {
    private val _jokes = mutableStateListOf<Joke>()
    val jokes: List<Joke> = _jokes

    private val _favorites = mutableStateListOf<Joke>()
    val favorites: List<Joke> = _favorites

    init {
        resetJokes()
    }

    fun favoriteJoke(joke: Joke) {
        if (!_favorites.contains(joke)) {
            _favorites.add(joke)
        }
    }

    fun removeJoke(joke: Joke) {
        _jokes.remove(joke)
    }

    fun removeFavorite(joke: Joke) {
        _favorites.remove(joke)
    }

    fun resetJokes() {
        _jokes.clear()
        _jokes.addAll(
            listOf(
                Joke("Why did the chicken cross the road?", "To get to the other side!"),
                Joke("What do you call a fake noodle?", "An impasta!"),
                Joke("How do you organize a space party?", "You planet!"),
                Joke("Why did the bicycle fall over?", "It was two-tired!"),
                Joke("What did the janitor say when he jumped out of the closet?", "Supplies!"),
                Joke("Why did the math book look sad?", "Because it had too many problems!"),
                Joke("What do you call a fish with no eyes?", "Fsh!"),
                Joke("Why did the scarecrow win an award?", "Because he was outstanding in his field!"),
                Joke("What do you call a pony with a sore throat?", "A little horse!"),
                Joke("Why did the tomato turn red?", "Because it saw the salad dressing!"),
                Joke("Did you hear about the mathematician who's afraid of negative numbers?", "He'll stop at nothing to avoid them!"),
                Joke("What do you call a bear with no teeth?", "A gummy bear!"),
                Joke("Did you hear about the two silk worms in a race?", "It ended in a tie!"),
                Joke("Why don't skeletons fight each other?", "They don't have the guts!"),
                Joke("What's the best thing about Switzerland?", "I don't know, but the flag is a big plus!"),
                Joke("Did you hear about the two pairs of scissors?", "They're both the same!"),
                Joke("Why don't eggs tell jokes?", "They'd crack each other up!"),
                Joke("Did you hear about the actor who fell through the floorboards?", "He was just going through a stage!"),
                Joke("Did you hear about the circus fire?", "It was in tents!"),
                Joke("What do you call a factory that makes okay products?", "A satisfactory!"),
                Joke("Did you hear about the guy who invented Lifesavers?", "They say he made a mint!"),
                Joke("I went to buy some camouflage pants","but I couldn't find any"),
                Joke("What's Forrest Gump's password?", "1Forrest1"),
                Joke("What do you call a belt made out of watches?", "A waist of time."),
                Joke("How do you follow Will Smith in the snow?", "You follow the fresh prints."),
                Joke("Did you hear the rumor about butter?", "Well, I'm not going to spread it."),
                Joke("How does a penguin build its house?", "Igloos it together"),
                Joke("Did you hear the one about the guy with the broken hearing aid?", "Neither did I, he said."),
                Joke("What did the ocean say to the beach?", "Nothing, it just waved."),
                Joke("I had a dream that I was a muffler last night.", "I woke up exhausted!")
            )
        )
    }
}










