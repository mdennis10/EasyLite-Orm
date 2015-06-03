# EasyLite-Orm
Very simple Object Relationship Mapping framework (ORM) for Android. 

##Configuration
```xml
<application>
    <meta-data android:name="DATABASE" android:value="app.db" />
    <meta-data android:name="VERSION" android:value="2" />
    <meta-data android:name="MODEL_PACKAGE_NAME" android:value="com.easylite.model" />
</application>
```

##Entity Models
<pre>
@Entity
public class Note {
	@Id(strategy = GenerationType.AUTO)
	public int id;
	public String body;
	public String author;
}
</pre>

##Usage
Get singleton instance of EasyLite to create Data Access Object
<pre>
Dao<Integer, Note> dao = EasyLite.getInstance(context)
                        .getDao(Note.class);
</pre>

Once dao is create use it for database operations
<pre>
Note note = new Note ();
dao.create(note);

List<Note> notes = dao.findAll();
notes = dao.findAll(orderBy,OrderByType.ASC,"author=?",note.author);
</pre>

