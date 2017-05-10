# MediaTrackingCenter

A small program used to track personal inventory of movies, books, and albums.  
Allows for searching entered records to compile a smaller list for browsing.
Each type of media provides a lookup function to search for common meta data.
Includes a link to meta data source for more information than is present in the tracker.

Run these MySQL commands to set up the database and user:
CREATE DATABASE mediaTracking; CREATE USER 'bradv'@'localhost' IDENTIFIED BY 'mediaTracking';GRANT ALL PRIVILEGES ON mediaTracking.* TO 'bradv'@'localhost';