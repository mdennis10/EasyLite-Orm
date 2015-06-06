# EasyliteOrm  [![Build Status](https://travis-ci.org/mdennis10/EasyLite-Orm.svg?branch=master)](https://travis-ci.org/mdennis10/EasyLite-Orm)  [![Coverage Status](https://coveralls.io/repos/mdennis10/EasyLite-Orm/badge.svg)](https://coveralls.io/r/mdennis10/EasyLite-Orm)

Very simple Object Relationship Mapping framework (ORM) for Android. 

## Features

- Very minimal configuration.
- Annotation driven data modeling 
- POJO Entity Classes that are loosely coupled
- Use of Data Access Objects for CRUD operations 

##Installation
####Gradle
<pre>
compile 'com.easyliteorm:easyliteorm:1.0.1'
</pre>

####Maven
```xml
<dependency>
  <groupId>com.easyliteorm</groupId>
  <artifactId>easyliteorm</artifactId>
  <version>1.0.1</version>
</dependency>
```

##Basic Setup
#####Configuration 
Add information about datababse to AndroidManifest.xml
```xml
<application>
    <meta-data android:name="DATABASE" android:value="dbname.db" />
    <meta-data android:name="VERSION" android:value="1" />
    <meta-data android:name="MODEL_PACKAGE_NAME" android:value="com.somepackagename.model" />
</application>
```
Define Entity Model
<pre>
@Entity
public class Note {
	@Id(strategy = GenerationType.AUTO)
	public int id;
	public String body;
	public String author;
}
</pre>

#####Usage
Get singleton instance of EasyLite to create Data Access Object (DAO)
<pre>
Dao<Integer, Note> dao = EasyLite.getInstance(context)
                                 .getDao(Note.class);
</pre>

Once dao is created, use it for database operations
<pre>
Note note = new Note ();
dao.create(note);

List<Note> notes = dao.findAll();
List<Note> notesByArtist = dao.findAll(orderBy,OrderByType.ASC,"author=?",note.author);
</pre>

[Get Started with EasyliteOrm](https://github.com/mdennis10/EasyLite-Orm/wiki)!
