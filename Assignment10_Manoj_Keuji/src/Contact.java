import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

class ContactService {
	ArrayList<Contact> contArr=new ArrayList<Contact>();
	public static void main(String[] args) throws Exception
	{
		ContactService obj=new ContactService();
		
		System.out.println("************* Adding Contact to Array **************");
		List<String> c1ctno=new ArrayList<String>();
		c1ctno.add("932094094");
		c1ctno.add("942099894");
		Contact c1=new Contact(33,"rameshbabu","rameshbabu@gmail.com",c1ctno);
		Contact c2=new Contact(38,"babu","	babu@gmail.com",c1ctno);
		Contact c3=new Contact(22,"ramnan","ramnaji@gmail.com",c1ctno);
		Contact c4=new Contact(88,"anajni","anjaniu@gmail.com",c1ctno);
		Contact c5=new Contact(99,"rutikl","rutikbabu@gmail.com",c1ctno);
		obj.addContact( c1, obj.contArr);
		obj.addContact( c3, obj.contArr);
		obj.addContact( c4, obj.contArr);
		obj.addContact( c5, obj.contArr);
		for(Contact c : obj.contArr)
		{System.out.println(c.toString());	}
		
		System.out.println("\n ************* Removing Contact to Array **************");
		//obj.RemoveContact(c1,obj.contArr);
		
		System.out.println("\n ************* Seach Contact by name **************");
		Contact c7=obj.searchContactByName("rameshbabu",obj.contArr);
		System.out.println("ContactFound:"+c7.toString());
		
		System.out.println("\n ************* Seach Contact as Contact number substring **************");
		List <Contact> ConList=obj.SearchContactByNumber("209",obj.contArr);
		System.out.println("Contact List Containg 209 as suubNumber:"+ConList.toString());
		
		System.out.println("\n ************* Add Contact number to current Contact obj list  **************");
		obj.addContactNumber(33,"209272724",obj.contArr);
		System.out.println(" 2092722724 Contact number added to id 33 :"+obj.contArr.toString());
		
		System.out.println("\n ************* Sort List by Contcat Name **************");
		obj.sortContactsByName(obj.contArr);
		
		System.out.println("\n ************* records added from Contact.txt  **************");
		obj.readContactsFromFile(obj.contArr,"Contact.txt");
		System.out.println(obj.contArr.toString());
		
		System.out.println("\n ************* records serialized to ContSerialized.txt  **************");
		obj.serializeContactDetails(obj.contArr,"ContSerialized.txt");
		
		System.out.println("\n ************* records Deserialized from ContSerialized.txt  **************");
		List <Contact> DList=obj.deserializeContact("ContSerialized.txt");
		System.out.println(DList.toString());
	
		System.out.println("\n ************* records populated from db to Set  **************");
		Set<Contact> nCSet=obj.populateContactFromDb();
		System.out.println(nCSet.toString());
		
		System.out.println("\n ************* Add Recoreds from set to ArrayList **************");
		obj.addContacts(obj.contArr,nCSet);
		System.out.println(obj.contArr.toString());
	}
	
	void addContact(Contact contact,List<Contact> contacts)
	{contacts.add(contact);}
	
	void RemoveContact(Contact contact, List<Contact> contacts) throws Exception{
		if(contacts.remove(contact))
		{	System.out.println("Removed Successfully");
			return;
		}
		else
		{throw new Exception("ContactNotFoundException");		}
	}
	
	Contact searchContactByName(String name, List<Contact> contact) throws Exception
	{
		for(Contact c : contact)
		{
			if(c.getContactName().equals(name))
			{
				return c;
			}
		}
		throw new Exception("ContactNotFoundException");
	}
	
	List<Contact> SearchContactByNumber(String number, List<Contact> contact) throws Exception
	{
		List<Contact> clist=new ArrayList<Contact>();
		for(Contact c : contact)
		{
			for(String sNo: c.getContactNumber())
			{
				if(sNo.contains(number))
				{
					clist.add(c);
					break;
				}
			}
		}
		return clist;	
	}
	
	void addContactNumber(int contactId, String contactNo, List<Contact> contacts)
	{	for(Contact c : contacts)
		{
				if(c.getContactID()==contactId)
				{
					List<String> cStr=c.getContactNumber();
					cStr.add(contactNo);
					break;
				}
		}
	}
	
	void sortContactsByName(List<Contact> contacts)
	{
		Collections.sort(contacts, new NameCompare());
		System.out.println(contacts.toString());
	}
	
	class NameCompare implements Comparator<Contact>
	{
	    public int compare(Contact m1, Contact m2)
	    {
	        return m1.getContactName().compareTo(m2.getContactName());
	    }
	}
	
	void readContactsFromFile(List<Contact> contacts, String fileName) throws Exception
	{
		File file=new File("C:\\Users\\Manoj\\eclipse-workspace\\Assignment10_Manoj_Keuji\\"+fileName);    //creates a new file instance  
        FileReader fr = null;
		fr = new FileReader(file);
        BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
        String sb;  
        String line; 
        try {
			while((line=br.readLine())!=null)  
			{ 
			sb=line;
			int i=sb.indexOf(",");
			String ids=sb.substring(0,i);
			int id= Integer.parseInt(ids);  
			
			int j=sb.indexOf(",",i+1);
			String name=sb.substring(i+1, j);
			i=j;
			String email;
			if(sb.indexOf(",",i+1)>1)
			{
			j=sb.indexOf(",",i+1);
			email=sb.substring(i+1, j);
			i=j;
			}
			else
			{
				email=sb.substring(i+1);
			}
			List <String> conL=null;
			if(sb.indexOf(",",i+1)>1)
			{
			j=sb.indexOf(",",i+1);
			String listCon=sb.substring(i+1);
			
			String[] elements = listCon.split(",");
			List<String> fixedLenghtList = Arrays.asList(elements);
			conL=new ArrayList<String>(fixedLenghtList);
			}
			Contact m=new Contact(id,name,email,conL);
			contacts.add(m);
			     
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	void serializeContactDetails(List<Contact> contacts , String filename)
	{

		 try
	        {
			 	File f=new File(filename);
			 	if(!f.exists())
			 	{
			 		f.createNewFile();
			 	}
	            FileOutputStream fos = new FileOutputStream(f);
	            ObjectOutputStream oos = new ObjectOutputStream(fos);
	            oos.writeObject(contacts);
	            oos.close();
	            fos.close();
	            System.out.println("Done");
	        } 
	        catch (IOException ioe) 
	        {
	            ioe.printStackTrace();
	        }
	}
	
	List<Contact> deserializeContact(String filename)
	{
		ArrayList<Contact> namesList = new ArrayList<Contact>();
        
        try
        {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            namesList = (ArrayList<Contact>)ois.readObject();
 
            ois.close();
            fis.close();
        } 
        catch(Exception e)
        {
        	System.out.println(e);
        }
         
        //Verify list data
        return namesList;
	}
	
	Set<Contact> populateContactFromDb() throws Exception
	{
		//Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/manoj","root","root");  
		Set<Contact> cSet=new HashSet<Contact>();
		//here sonoo is database name, root is username and password 
		try {
		
			Statement stmt = con.createStatement();
			Statement stmt2= con.createStatement();

		      String sql = "SELECT * from contact ";
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	  
		    	  String mail=rs.getString("mail");
		    	  String name=rs.getString("contacName");
		    	  int ids=rs.getInt("idcontact");
		    	  String sql2 = "SELECT * from connumber where idconNumber= "+rs.getInt("idcontact");
			      ResultSet rs2 = stmt2.executeQuery(sql2);
			     
			      List<String> l=new ArrayList<String>();
			      while(rs2.next())
			      {
			    	  l.add(rs2.getString("conNo"));
			      }
			      
			      Contact cnew=new Contact(ids,name,mail,l);
			      cSet.add(cnew);
		      }
		      }
		catch(Exception e) {
			System.out.println(e);
		}
		return cSet;     		        
		}
	
	Boolean addContacts(List<Contact> existingContact,Set<Contact> newContacts)
	{
		existingContact.addAll(newContacts);
		return null;
		
	}
	
}

class Contact implements Serializable{
	private int contactID;
	private String contactName;
	private String emailAddress;
	private List<String> contactNumber;
	
	
	public Contact(int contactID, String contactName, String emailAddress, List<String> contactNumber) {
		super();
		this.contactID = contactID;
		this.contactName = contactName;
		this.emailAddress = emailAddress;
		this.contactNumber = contactNumber;
	}
	
	
	

	

	public int getContactID() {
		return contactID;
	}
	public void setContactID(int contactID) {
		this.contactID = contactID;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public List<String> getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(List<String> contactNumber) {
		this.contactNumber = contactNumber;
	}
	@Override
	public String toString() {
		return "\n Contact [contactID=" + contactID + ", contactName=" + contactName + ", emailAddress=" + emailAddress
				+ ", contactNumber=" + contactNumber + "]";
	}
	
	 
	

}

