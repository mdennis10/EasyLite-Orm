# EasyliteORM  [![Build Status](https://travis-ci.org/mdennis10/EasyLite-Orm.svg?branch=master)](https://travis-ci.org/mdennis10/EasyLite-Orm)  [![Coverage Status](https://coveralls.io/repos/mdennis10/EasyLite-Orm/badge.svg)](https://coveralls.io/r/mdennis10/EasyLite-Orm)

Very simple Object Relationship Mapping framework (ORM) for Android. 

## Features

- Very minimal configuration.
- Annotation driven data modeling 
- POJO Entity Classes that are loosely coupled
- Use of Data Access Objects for CRUD operations 
- Asynchronous Operations

##Installation
####Gradle
<pre>
compile 'com.easyliteorm:easyliteorm:1.2.0'
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
```xml

Dao<Integer, Note> dao = EasyLite.getInstance(context)
                                 .getDao(Note.class);
Note note = new Note ();
dao.create(note);
List<Note> notes = dao.findAll();

```

######Asynchronous 
version 1.2.0 and greater
```xml
dao.findAllAsync(new ResponseListener<List<Note>>() {
			@Override
			public void onComplete(List<Note> result) {
				// do something with result
			}
		});
```

###License
The MIT License (MIT)

Copyright (c) 2015 Mario Dennis

http://www.opensource.org/licenses/mit-license.php

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:



[Get Started with EasyliteOrm](https://github.com/mdennis10/EasyLite-Orm/wiki)!
