# EasyLite-Orm
Very simple Object Relationship Mapping framework (ORM) for Android. 

##Configuration
<application>
    <meta-data android:name="DATABASE" android:value="app.db" />
    <meta-data android:name="VERSION" android:value="2" />
    <meta-data android:name="MODEL_PACKAGE_NAME" android:value="com.easylite.model" />
</application>

##Entity Models
@Entity
public class Note {
	@Id(strategy = GenerationType.AUTO)
	public int id;
	public String body;
	public String author;
}

##Usage
Dao<Integer, Note> dao = EasyLite.getInstance(activity)
                                 .getDao(Note.class);
                                 
Note note = new Note ();
dao.create(note);
