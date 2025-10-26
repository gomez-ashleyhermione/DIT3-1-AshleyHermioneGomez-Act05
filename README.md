Reflection:

-Reflection 1: How did you implement CRUD using SQLite?

To implement CRUD operations, I first created a DatabaseHelper class that extends SQLiteOpenHelper. This helper class manages the database creation and all the operations.
For the database structure, I defined a table called "notes" with four columns: id (primary key that auto-increments), title, content, and timestamp.
I set this up in the onCreate() method using SQL commands.

For Create, I used the insert() method with ContentValues to add new notes. I had to put each field (title, content, timestamp) 
into the ContentValues object before inserting it into the database.
For Read, I used rawQuery() with a SELECT statement to get all notes from the database. 
The query returns a Cursor object, which I had to loop through to convert each row into a Note object. 
I made sure to order them by timestamp in descending order so the newest notes appear first.

For Update, I used the update() method.
I passed in the note ID to identify which note to update, and used ContentValues
again to specify the new title and content.

For Delete, I used the delete() method with the note ID as a parameter.
I also added a confirmation dialog in MainActivity so users don't accidentally delete notes.

-Reflection 2: What challenges did you face in maintaining data persistence?

At first, I had trouble understanding how the database actually stays persistent. 
I thought I needed to manually save it somewhere, but then I realized SQLite handles that automatically once you create the database file.
One challenge I faced was with the getColumnIndexOrThrow() method when reading data from the Cursor.
I kept getting errors because I spelled the column name wrong ("content" vs "contents"). The error messages weren't super clear at first, so it took me a while to debug.

Another issue was that my RecyclerView wasn't updating automatically after adding or editing notes. I had to call loadNotes() in the onResume() method of MainActivity so that every time I come back from the editor activity, the list refreshes with the latest data from the database.

I also initially forgot to handle the case when the database is empty. The app would just show a blank screen, which looked broken.
I fixed this by adding an empty state TextView that shows "No notes yet" when there are no notes in the database.

The timestamp formatting was tricky too. At first, I just displayed the raw timestamp number, 
which looked really ugly. I had to use SimpleDateFormat to convert it into a readable date like "Oct 26, 2025 10:30 AM".

-Reflection 3: How could you improve performance or UI design in future versions?

Performance improvements:

-Add a search feature so users can quickly find notes by title or content. Right now, if you have 100 notes, you'd have to scroll through all of them.
-Implement pagination or lazy loading. If someone has thousands of notes, loading them all at once could slow down the app.
-Add database indexing on the timestamp column to make queries faster.

UI/UX improvements:

-Add categories or tags so users can organize notes (like "Work", "Personal", "School").
-Implement a color-coding system where users can assign different colors to notes.
-Include a confirmation prompt when users try to leave the editor with unsaved changes.
-Add swipe-to-delete gesture or a delete logo instead of long press to delete.
-Implement a dark mode toggle using SharedPreferences to remember the user's theme preference.
-Add a "last edited" indicator instead of just creation timestamp.

