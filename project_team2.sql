--MILESTONE 2

--query a
SELECT c.title, rt.runtime 
FROM runs_for rf, runningTimes rt, clips c, countries cn 
WHERE c.CLIPIDS=rf.clipid AND rf.rid=rt.rid AND rf.countid=cn.countid 
AND cn.COUNTRYNAME='France' 
ORDER BY rt.runtime DESC
FETCH FIRST 10 ROWS ONLY;

--query b
SELECT cn.countryname, COUNT(r.clipid) AS number_of_clips
FROM released_in r, releasedates rd, countries cn
WHERE r.did=rd.DID AND rd.RELEASEDATE='2001' AND cn.COUNTID=r.COUNTID
GROUP BY cn.countryname;

--query c
SELECT g.genre, COUNT(hg.clipid) AS number_of_clips
FROM clip_genre g, has_genre hg, released_in r, RELEASEDATES rd, countries cn
WHERE hg.gid=g.gid AND r.DID=rd.DID AND rd.RELEASEDATE>2013 AND r.countid=cn.countid 
AND cn.countryname='USA' AND r.CLIPID=hg.CLIPID
GROUP BY g.genre;

--query d
SELECT fullname FROM
(SELECT p.fullname, COUNT(ac.clipids) AS number_of_clips
FROM people p, act ac, clips c
WHERE p.pid=ac.PID and ac.CLIPIDS=c.CLIPIDS
GROUP BY p.fullname
ORDER BY number_of_clips DESC
FETCH FIRST 1 ROWS ONLY);

--query e
SELECT number_of_clips FROM
(SELECT d.pid, COUNT(d.clipids) AS number_of_clips
FROM direct d, clips c
WHERE d.CLIPIDS=c.CLIPIDS
GROUP BY d.pid
ORDER BY number_of_clips DESC
FETCH FIRST 1 ROWS ONLY); 

--query f
SELECT people.fullname
FROM people
WHERE people.pid IN
(SELECT pid FROM
(SELECT pid, Clipids FROM act GROUP BY pid, Clipids
UNION ALL
SELECT pid, Clipids FROM direct
UNION ALL
SELECT pid, Clipids FROM produce
UNION ALL
SELECT pid, Clipids FROM write)
GROUP BY pid, clipids
HAVING COUNT(*) > 1);

--query g
SELECT language FROM
(SELECT cl.language, COUNT(hl.clipid) AS number_of_clips
FROM clip_languages cl, has_language hl
WHERE cl.lid=hl.lid
GROUP BY cl.language
ORDER BY number_of_clips DESC
FETCH FIRST 10 ROWS ONLY);

-- query h
--suppose the user specified type is Fantasy
SELECT fullname FROM
(SELECT p.fullname, COUNT(ac.clipids) AS number_of_clips
FROM people p, act ac, clips c, has_genre hg, clip_genre g
WHERE p.pid=ac.PID AND ac.CLIPIDS=c.CLIPIDS 
AND c.clipids=hg.clipid AND hg.gid=g.gid AND g.GENRE='Fantasy'
GROUP BY p.fullname
ORDER BY number_of_clips DESC
FETCH FIRST 1 ROWS ONLY);

SELECT * FROM CLIP_GENRE WHERE GENRE='Sport';
UPDATE CLIP_GENRE SET GENRE=TRIM(TRAILING ' ' FROM GENRE);

--MILESTONE 3

--query a
