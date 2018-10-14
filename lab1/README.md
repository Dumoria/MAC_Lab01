# How to generate a zip archive to upload to moodle/cyberlearn?

```
git checkout students
git archive --format=zip --prefix=lab1/ HEAD > lab1.zip
```

Please don't commit the archive file.
