import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class PredefinedQueriesPanel extends JPanel{

	private JTextArea result = new JTextArea(10, 50);
	private JLabel labelGenre = new JLabel("Genre: ");
	private JTextField genre = new JTextField(10);
	private JScrollPane resultScroll = new JScrollPane(result);
	private JLabel label = new JLabel("Queries:");
	private JComboBox<String> combo = new JComboBox<String>();
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private JLabel feedback = new JLabel();
	private JButton ok = new JButton("Execute");

	private Connection conn = null;

	public static String[] queries = {"QUERY1", "QUERY2", "QUERY3", "QUERY4",
			"QUERY5", "QUERY6", "QUERY7", "QUERY8","QUERY9", "QUERY10", 
			"QUERY11", "QUERY12","QUERY13", "QUERY14", "QUERY15", "QUERY16", 
			"QUERY17", "QUERY18", "QUERY19", "QUERY20"};

	public PredefinedQueriesPanel(){

		add(label);
		Fenetre.fillComboWithStrings(combo, queries);
		add(combo);


		combo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				String query = (String) combo.getSelectedItem();
				if(query.equals("QUERY8")) {
					JPanel genrePanel = new JPanel();
					genrePanel.add(labelGenre);
			  		genrePanel.add(genre);
			  		add(genrePanel, BorderLayout.SOUTH);
				}
				System.out.println("Hello");
			}
		});

		ok.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				Statement st = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					String query = (String) combo.getSelectedItem();
					String sql = ""; //demander a guiti de m'envoyer les commandes
					switch (query){
					  case "QUERY1":
					    sql = "SELECT c.TITLE, rt.RUNTIME " + 
					    		"FROM RUNS_FOR rf, RUNNINGTIMES rt, CLIPS c, COUNTRIES cn " + 
					    		"WHERE c.CLIPIDS=rf.CLIPID AND rf.RID=rt.RID AND rf.COUNTID=cn.COUNTID " + 
					    		"AND cn.COUNTRYNAME='France' " + 
					    		"ORDER BY rt.RUNTIME DESC " + 
					    		"FETCH FIRST 10 ROWS ONLY";
					    break;
					  case "QUERY2":
						sql = "SELECT cn.COUNTRYNAME, COUNT(r.CLIPID) AS number_of_clips " + 
								"FROM RELEASED_IN r, RELEASEDATES rd, COUNTRIES cn " + 
								"WHERE r.DID=rd.DID AND rd.RELEASEDATE='2001' AND cn.COUNTID=r.COUNTID " + 
								"GROUP BY cn.COUNTRYNAME";
						break;
					  case "QUERY3":
						sql = "SELECT g.GENRE, COUNT(hg.CLIPID) AS number_of_clips " + 
								"FROM CLIP_GENRE g, HAS_GENRE hg, RELEASED_IN r, RELEASEDATES rd, COUNTRIES cn " + 
								"WHERE hg.GID=g.GID AND r.DID=rd.DID AND rd.RELEASEDATE>2013 AND r.COUNTID=cn.COUNTID " + 
								"AND cn.COUNTRYNAME='USA' AND r.CLIPID=hg.CLIPID " + 
								"GROUP BY g.GENRE";
						break;
					  case "QUERY4":
						sql = "SELECT FULLNAME FROM " + 
								"(SELECT p.FULLNAME, COUNT(ac.CLIPIDS) AS number_of_clips " + 
								"FROM PEOPLE p, ACT ac, CLIPS c " + 
								"WHERE p.PID=ac.PID and ac.CLIPIDS=c.CLIPIDS " + 
								"GROUP BY p.FULLNAME " + 
								"ORDER BY number_of_clips DESC " + 
								"FETCH FIRST 1 ROWS ONLY)";
						break;
					  case "QUERY5":
						sql = "SELECT number_of_clips FROM " + 
								"(SELECT d.PID, COUNT(d.CLIPIDS) AS number_of_clips " + 
								"FROM DIRECT d, CLIPS c " + 
								"WHERE d.CLIPIDS=c.CLIPIDS " + 
								"GROUP BY d.PID " + 
								"ORDER BY number_of_clips DESC " + 
								"FETCH FIRST 1 ROWS ONLY)";
						break;
					  case "QUERY6":
						sql = "SELECT PEOPLE.fullname " + 
								"FROM PEOPLE " + 
								"WHERE PEOPLE.pid IN " + 
								"(SELECT pid FROM " + 
								"(SELECT pid, CLIPIDS FROM ACT GROUP BY pid, CLIPIDS " + 
								"UNION ALL " + 
								"SELECT pid, CLIPIDS FROM DIRECT " + 
								"UNION ALL " + 
								"SELECT pid, CLIPIDS FROM PRODUCE " + 
								"UNION ALL " + 
								"SELECT pid, CLIPIDS FROM WRITE) " + 
								"GROUP BY pid, clipids " + 
								"HAVING COUNT(*) > 1)";
						break;
					  case "QUERY7":
						sql = "SELECT language FROM " + 
								"(SELECT cl.language, COUNT(hl.clipid) AS number_of_clips " + 
								"FROM CLIP_LANGUAGES cl, HAS_LANGUAGE hl " + 
								"WHERE cl.lid=hl.lid " + 
								"GROUP BY cl.language " + 
								"ORDER BY number_of_clips DESC " + 
								"FETCH FIRST 10 ROWS ONLY)";
						break;
				      case "QUERY8":
				  		String userGenre = "";
				  		if(genre.getText().length() > 0){
							userGenre = genre.getText();
						}
						sql = "SELECT fullname FROM "+ 
								"(SELECT p.fullname, COUNT(ac.clipids) AS number_of_clips " + 
								"FROM PEOPLE p, ACT ac, CLIPS c, HAS_GENRE hg, CLIP_GENRE g " + 
								"WHERE p.PID=ac.PID AND ac.CLIPIDS=c.CLIPIDS " + 
								"AND c.clipids=hg.clipid AND hg.gid=g.gid AND g.GENRE='"+userGenre+"' " + 
								"GROUP BY p.fullname " + 
								"ORDER BY number_of_clips DESC " + 
								"FETCH FIRST 1 ROWS ONLY)";
						break;
					  case "QUERY9":
						sql = "select p.fullname\n" + 
								"from people p\n" + 
								"where p.pid in\n" + 
								"    (select pid from\n" + 
								"      (select ac.pid,c.rank, row_number()\n" + 
								"      over (partition by ac.pid order by c.rank desc) as index_clip_per_actor\n" + 
								"      from clips c, act ac\n" + 
								"      where ac.CLIPIDS=c.CLIPIDS and c.votes>100 and ac.pid in\n" + 
								"        (select pid from\n" + 
								"          (select distinct ac.pid, ac.clipids\n" + 
								"          from act ac)\n" + 
								"        group by pid\n" + 
								"        having count(*)>5))\n" + 
								"        where index_clip_per_actor<4\n" + 
								"        group by pid\n" + 
								"        order by avg(rank) desc\n" + 
								"        FETCH FIRST 10 ROWS ONLY)";
						break;
					  case "QUERY10":
						sql = "select decade, avg(rank) from\n" + 
								"  (select clipids, rank, decade,\n" + 
								"  row_number() over (partition by decade order by rank desc) as index_clip_per_decade\n" + 
								"  from\n" + 
								"    (select CLIPIDS, rank, floor(year/ 10) * 10 as decade\n" + 
								"    from clips\n" + 
								"    where rank is not null and year is not null))\n" + 
								"where index_clip_per_decade < 101\n" + 
								"group by decade\n" + 
								"order by avg(rank) desc";
						break;
					  case "QUERY11":
						sql = "select fullname, year, game_titles from\n" + 
								"  (select year, fullname, game_titles, \n" + 
								"  row_number() over (partition by fullname order by year) as index_year_per_pid\n" + 
								"  from\n" + 
								"    (select c.year, p.fullname,\n" + 
								"    listagg(c.title, ' , ') within group (order by c.title) as game_titles\n" + 
								"    from direct d, clips c, people p\n" + 
								"    where d.clipids=c.CLIPIDS and p.pid=d.pid and c.type='VG'\n" + 
								"    group by p.fullname,c.year))\n" + 
								"where index_year_per_pid=1";
						break;
					  case "QUERY12":
						sql = "select year,\n" + 
								"listagg(title, ' / ') within group (order by rank desc) as top3_clips_titles,\n" + 
								"listagg(rank, ' / ') within group (order by rank desc) as top3_clips_ranks\n" + 
								"from\n" + 
								"  (select year, title, rank,\n" + 
								"  row_number() over (partition by year order by rank desc) as index_clip_per_year\n" + 
								"  from clips\n" + 
								"  where year is not null and rank is not null)\n" + 
								"where index_clip_per_year<4\n" + 
								"group by year";
						break;
					  case "QUERY13":
						sql = "select p.fullname from people p,\n" + 
								"    (with T as\n" + 
								"      (select d.pid, c.rank\n" + 
								"      from direct d, clips c\n" + 
								"      where c.clipids=d.clipids and c.rank is not null\n" + 
								"      and d.pid in (select distinct pid from write)\n" + 
								"      and d.pid not in\n" + 
								"        (select distinct pid from\n" + 
								"            (select w.pid, w.clipids from write w\n" + 
								"            where (w.pid, w.clipids) not in\n" + 
								"            (select ac.pid, ac.clipids from act ac))))\n" + 
								"    select T.pid, min(T.rank) as min_rank_directed, max(c.rank) as max_rank_written\n" + 
								"    from T, write w, clips c\n" + 
								"    where w.pid=T.pid and w.clipids=c.clipids and c.rank is not null\n" + 
								"    group by T.pid) S\n" + 
								"where S.min_rank_directed > S.max_rank_written+2 and p.pid=S.pid";
						break;
					  case "QUERY14":
					    sql = "select p.fullname\n" + 
					    		"from people p, married m\n" + 
					    		"where p.pid not in m.PID and p.pid in\n" + 
					    		"(select pid from\n" + 
					    		"  (select pid, count(*) as nbr_clips_he_acted_directed from\n" + 
					    		"    (select pid, clipids from\n" + 
					    		"      (select pid, clipids, count(*) as number_of_jobs from\n" + 
					    		"        (SELECT pid, Clipids FROM act GROUP BY pid, Clipids\n" + 
					    		"        UNION ALL\n" + 
					    		"        SELECT pid, Clipids FROM direct)\n" + 
					    		"      group by pid , clipids)\n" + 
					    		"    where number_of_jobs>1)\n" + 
					    		"  group by pid)\n" + 
					    		"where nbr_clips_he_acted_directed>2)";
						break;
					  case "QUERY15":
						sql = "select fullname from people where PID in\n" + 
								"  (select w.pid from\n" + 
								"    (select w.pid, w.clipids\n" + 
								"    from write w \n" + 
								"    where w.worktypes='screenplay') w\n" + 
								"  left outer join\n" + 
								"    (select p.clipids, count(*) as number_of_producers\n" + 
								"    from produce p\n" + 
								"    group by p.clipids) p\n" + 
								"  on w.clipids=p.clipids\n" + 
								"  where number_of_producers>2)";
						break;
					  case "QUERY16":
						sql = "WITH T As\n" + 
								"  (select pid, avg(rank) as average from\n" + 
								"    (select distinct ac.pid, ac.clipids, ac.orderscredit, c.rank\n" + 
								"    from act ac, clips c\n" + 
								"    where orderscredit<4 and orderscredit>0 and rank is not null and c.clipids=ac.clipids)\n" + 
								"  group by pid)\n" + 
								"SELECT p.fullname, T.average \n" + 
								"FROM people p, T\n" + 
								"WHERE p.pid=T.pid";
						break;
					  case "QUERY17":
						sql = "select avg(c.rank)\n" + 
								"from clips c, has_genre hg\n" + 
								"where hg.clipid=c.CLIPIDS and hg.gid in\n" + 
								"  (select gid from\n" + 
								"    (select gid, count(*) as nbr_clips\n" + 
								"    from has_genre\n" + 
								"    group by gid\n" + 
								"    order by nbr_clips desc\n" + 
								"    FETCH FIRST 1 ROWS ONLY))\n" + 
								"group by hg.gid";
						break;
					  case "QUERY18":
						sql = "select p.fullname, l.nbr_of_comedy_clips, l.nbr_of_Drama_clips from people p,\n" + 
								"  (select distinct s1.pid, s1.nbr_of_clips_per_genre as nbr_of_comedy_clips, s2.nbr_of_clips_per_genre as nbr_of_Drama_clips\n" + 
								"  from\n" + 
								"  (select pid, genre, count(*) as nbr_of_clips_per_genre from\n" + 
								"    (select pid, cg.genre \n" + 
								"    from act a, clip_genre cg, has_genre hg\n" + 
								"    where a.clipids=hg.clipid and hg.gid=cg.gid and (cg.genre='Comedy' or cg.genre='Drama'))\n" + 
								"  group by pid, genre) s1\n" + 
								"  join\n" + 
								"  (select pid, genre, count(*) as nbr_of_clips_per_genre from\n" + 
								"    (select pid, cg.genre \n" + 
								"    from act a, clip_genre cg, has_genre hg\n" + 
								"    where a.clipids=hg.clipid and hg.gid=cg.gid and (cg.genre='Comedy' or cg.genre='Drama'))\n" + 
								"  group by pid, genre) s2\n" + 
								"  on s1.pid=s2.pid\n" + 
								"  where s1.genre='Comedy' and s1.nbr_of_clips_per_genre>2*s2.nbr_of_clips_per_genre and s2.genre='Drama'\n" + 
								"  and s1.pid in\n" + 
								"  (select R.pid from\n" + 
								"    (select pid, genre, nbr_of_clips_of_actor, count(*) as nbr_of_clips_per_genre\n" + 
								"      from\n" + 
								"        (with T as\n" + 
								"          (select distinct pid, nbr_of_clips_of_actor from\n" + 
								"            (select a.pid, count(*) as nbr_of_clips_of_actor\n" + 
								"            from act a\n" + 
								"            group by a.pid)\n" + 
								"          where nbr_of_clips_of_actor>100)\n" + 
								"        select T.pid, T.nbr_of_clips_of_actor, cg.genre\n" + 
								"        from T, act a, clip_genre cg, has_genre hg\n" + 
								"        where T.pid=a.pid and a.clipids=hg.clipid and hg.gid=cg.gid)\n" + 
								"    group by pid, genre, nbr_of_clips_of_actor) R\n" + 
								"    where (R.nbr_of_clips_per_genre > 0.6 * R.nbr_of_clips_of_actor and R.genre='Short'))) l\n" + 
								"where p.pid=l.pid";
						break;
					  case "QUERY19":
						sql = "select count(*) as nbr_of_movies\n" + 
								"from CLIP_LANGUAGES cl, has_language hl, clips c, has_genre hg\n" + 
								"where cl.lid=hl.lid and cl.LANGUAGE='Dutch' and c.CLIPIDS=hl.CLIPID and \n" + 
								"c.CLIPIDS=hg.CLIPID and (c.type='V' or c.type='TV') and hg.gid in\n" + 
								"  (select gid from\n" + 
								"    (select gid, rownum as rn from\n" + 
								"      (select gid, count(*) as nbr_clips\n" + 
								"      from has_genre\n" + 
								"      group by gid\n" + 
								"      order by nbr_clips desc\n" + 
								"      FETCH FIRST 2 ROWS ONLY))\n" + 
								"  where rn=2)";
						break;
					  case "QUERY20":
						sql = "with T as\n" + 
								"  (select pid, count(*) as nbr_of_clips from\n" + 
								"    (select p.pid, p.CLIPIDS\n" + 
								"    from produce p, has_genre hg, clips c\n" + 
								"    where p.ROLES='coordinating producer' and p.CLIPIDS=hg.clipid and p.CLIPIDS=c.clipids \n" + 
								"    and (c.type='V' or c.type='TV') and hg.GID in \n" + 
								"      (select gid from\n" + 
								"        (select gid, count(*) as nbr_clips\n" + 
								"        from has_genre\n" + 
								"        group by gid\n" + 
								"        order by nbr_clips desc\n" + 
								"        FETCH FIRST 1 ROWS ONLY)))\n" + 
								"  group by pid\n" + 
								"  order by nbr_of_clips desc\n" + 
								"  FETCH first 1 row only)\n" + 
								"select p.fullname\n" + 
								"from people p, T\n" + 
								"where p.pid=T.pid";
						break;
					  default:
					    System.out.println("error");
					}
					
					ResultSet rs = st.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData();
					int noColumns = rsmd.getColumnCount();
					String resultString = "";
					int i = 0;
					while (rs.next() && i < 10) {
						for(int j = 1; j <= noColumns; ++j){
							resultString += (rs.getString(j) + '\t');
						}
						resultString += '\n';
						++i;
					}

					result.setText(resultString);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					try {
						conn.close();
						st.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});

		add(ok);

		JPanel center = new JPanel();
		center.add(resultScroll);
		add(center, BorderLayout.CENTER);

	}


}
