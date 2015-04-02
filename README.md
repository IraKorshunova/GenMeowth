This simple program fits a chordal accompaniment to a melody.
The melody is generated with a random walk on a graph of notes, which is constructed from:

1. a specified set of notes (can choose a key and time signature)
2. notes taken from another melody (can choose only the time signature)

Note in our case represents a relative duration and a pitch.

The harmonization is a rule-based with a very basic set of rules, which I knew in 2010 (the time it was written).

1. Load any file from a /midi folder 
2. Select a time signature, instrument and tempo 
3. Generate
4. Play

You may skip the 1st step, but select a key signature then. Be careful: it may sound bad. 
 
![screen](/screen.jpg)

I saved some results in tracks 1-4.
