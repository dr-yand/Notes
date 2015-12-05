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
      String userId = Request["userId"]; 
	  
	  if(userId==null){
		String response = "{response:\"" + NO_DATA + "\",}";
		Response.Write(response);
	  }
	  else{
		ConnectionStringSettings mySetting = ConfigurationManager.ConnectionStrings["DbConnection"];
		String connectionString = mySetting.ConnectionString; 
		using (SqlConnection cn = new SqlConnection(connectionString))
		{
          SqlCommand cmd = new SqlCommand("SELECT id, note FROM notes " +
              " WHERE user_id=@userId", cn); 
          cmd.Parameters.Add(new SqlParameter("userId", userId));
          cn.Open();
           
          SqlDataReader rdr = cmd.ExecuteReader();

		  String data = "[";
          while (rdr.Read()){
			  if(data.Length>1)
				data += ",";
              String id = rdr["id"].ToString();
              String note = rdr["note"].ToString(); 
              String info = String.Format("{{note:\"{0}\", id:\"{1}\"}}",
                              note, id);
			  data+=info;						
          } 
		  data+="]";
          String response = String.Format("{{response:{0}, data:{1}}}",
                              ALL_OK, data);
          Response.Write(response);

          rdr.Close();           
		} 
	  }
  }
</script>