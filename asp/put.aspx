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
	  String note = Request["note"]; 
	  
	  if(userId==null||note==null){
		String response = "{response:\"" + NO_DATA + "\",}";
		Response.Write(response);
	  }
	  else{
		ConnectionStringSettings mySetting = ConfigurationManager.ConnectionStrings["DbConnection"];
		String connectionString = mySetting.ConnectionString; 
		using (SqlConnection cn = new SqlConnection(connectionString))
		{
          SqlCommand cmd = new SqlCommand("INSERT INTO notes(user_id, note) " +
              " VALUES(@userId,@note)", cn); 
          cmd.Parameters.Add(new SqlParameter("userId", userId));
		  cmd.Parameters.Add(new SqlParameter("note", note));
          cn.Open();
           
          cmd.ExecuteNonQuery();

          String response = String.Format("{{response:{0}}}",
                              ALL_OK);
          Response.Write(response);
         
		} 
	  }
  }
</script>