# EasyLite-Orm

Very simple Object Relationship Mapping framework (ORM) for Android. 

## Features

- Very minimal configuration.
- Annotation driven data modeling 
- POJO Entity Classes that are loosely coupled
- Use of Data Access Objects for CRUD operations 

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

Get singleton instance of EasyLite to create Data Access Object (DAO)
<pre>
Dao<Integer, Note> dao = EasyLite.getInstance(context)
                        .getDao(Note.class);
</pre>

Once dao is createf use it for database operations
<pre>
Note note = new Note ();
dao.create(note);

List<Note> notes = dao.findAll();
notes = dao.findAll(orderBy,OrderByType.ASC,"author=?",note.author);
</pre>

