<%@ Page Language="C#" %>
<%@ Import Namespace="System.Data" %> 
<%@ Import Namespace="System.Data.SqlClient" %>
  
<script runat="server">
	public const int ALL_OK = 0;
	public const int NO_DATA = -1;
    public const int NO_USER = -2;
    public const int ERROR = -100;
	
  protected void Page_Load(object sender, EventArgs e)
  {
      String login = Request["login"];
      String password = Request["password"];
	  
	  if(login==null||password==null){
		String response = "{response:\"" + NO_DATA + "\",}";
		Response.Write(response);
	  }
	  else{
		ConnectionStringSettings mySetting = ConfigurationManager.ConnectionStrings["DbConnection"];
		String connectionString = mySetting.ConnectionString; 
		using (SqlConnection cn = new SqlConnection(connectionString))
		{
          SqlCommand cmd = new SqlCommand("SELECT id, name FROM users " +
              " WHERE login=@login AND password=@password", cn);
          cmd.Parameters.Add(new SqlParameter("login", login));
          cmd.Parameters.Add(new SqlParameter("password", password));
          cn.Open();
          
          String id = "0";
          String name = ""; 
          SqlDataReader rdr = cmd.ExecuteReader();
          int response = NO_USER;
          if (rdr.Read()){
              id = rdr["id"].ToString();
              name = rdr["name"].ToString(); 
              response = ALL_OK;
          } 
          String txt = String.Format("{{response:{0}, name:\"{1}\", id:\"{2}\"}}",
                              response, name, id);
          Response.Write(txt);

          rdr.Close();           
		} 
	  }
  }
</script>