import java.lang.*;
import edu.duke.*;
import java.io.*;
import org.apache.commons.csv.*;
/**
 * Write a description of Part1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Part1 {
   
    public void totalBirthInFile(CSVParser parser){
        int noOfGirlsName=0;
        int noOfBoysName=0;
        int totalNames=0;
        int noOfGirls=0;
        int noOfBoys=0;
        int totalPopulation=0;
        for(CSVRecord rec : parser){
            String gender = rec.get(1);
            String numberOfNames = rec.get(2);
            if(gender.equals("F")){
                noOfGirlsName++;
                noOfGirls+=Integer.parseInt(numberOfNames);
            }
            else{
                noOfBoysName++;
                noOfBoys+=Integer.parseInt(numberOfNames);
            }
        }
        totalNames=noOfGirlsName+noOfBoysName;
        totalPopulation = noOfGirls+noOfBoys;
        System.out.println("no of girls name - "+noOfGirlsName+" no of boys name - "+noOfBoysName+
                            " no of girls - "+noOfGirls+" no of boys - "+noOfBoys+
                             " total no of names - "+totalNames+" total population - "+totalPopulation);
    }
    
    public CSVParser getParser(String filePath){
        FileResource fr = new FileResource(filePath);
        CSVParser parser = fr.getCSVParser(false);
        return parser;
    }
    
    public int getRank(int year, String name, String gender){
        String path= "us_babynames_by_year/yob" + Integer.toString(year)+".csv";
        CSVParser parser = getParser(path);
        
        int girlsRank=0;
        int boysRank=0;
        for(CSVRecord rec :parser){
            if(rec.get(1).equals("F")){
                girlsRank++;
            }
            if(rec.get(1).equals("M")){
                boysRank++;
            }
            String currName= rec.get(0);
            if(currName.equals(name)&&gender.equals(rec.get(1))){
                if(gender.equals("F")){
                    return girlsRank;
                }
                if(gender.equals("M")){
                    return boysRank;
                }
            }
        }
        return -1;
    }
    
    
    public String getName(int year, int rank , String gender){
        String path= "us_babynames_by_year/yob" + Integer.toString(year)+".csv";
        CSVParser parser = getParser(path);
        int girlsRank=0;
        int boysRank=0;
        for(CSVRecord rec :parser){
            if(rec.get(1).equals("F")){
                girlsRank++;
            }
            if(rec.get(1).equals("M")){
                boysRank++;
            }
            String currName= rec.get(0);
            if(rank==boysRank && gender.equals("M")&&rec.get(1).equals("M")){
                return currName;
            }
            if(rank==girlsRank && gender.equals("F")&&rec.get(1).equals("F")){
                return currName;
            }
        }
        
        return "not found";
    }
    
    
    public int yearOfHighestRank(String name, String gender){
        DirectoryResource dr= new DirectoryResource();
        int minRank=0;
        int finalYear=-1;
        for(File f:dr.selectedFiles()){
            FileResource fr =new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            String fileName=f.getName();
            int currYear = Integer.parseInt(fileName.substring(3,7));
            int currRank = getRank(currYear,name,gender);
            
            if(minRank==0){
                if(currRank!=-1){
                    minRank=currRank;
                    finalYear=currYear;
                }
            }
            else{
                if(currRank!=-1&&currRank<minRank){
                    minRank=currRank;
                    finalYear=currYear;
                }
            }
            
        }
        return finalYear;
    }
    
    public String whatIsYourNameInYear(String name, int year, int newYear, String gender){
        int rank =getRank(year,name,gender);
        String newName=getName(newYear,rank,gender);
        return newName;
    }
    
    public double getAverageRank(String name, String gender){
        DirectoryResource dr= new DirectoryResource();
        int totalRank=-1;
        int count = 0;
        for(File f:dr.selectedFiles()){
            FileResource fr =new FileResource(f);
            CSVParser parser = fr.getCSVParser(false);
            String fileName=f.getName();
            int currYear = Integer.parseInt(fileName.substring(3,7));
            int currRank = getRank(currYear,name,gender);
            count++;
            if(totalRank==-1){
                if(currRank!=-1){
                    totalRank=currRank;
                }
            }
            else{
                if(currRank!=-1){
                    totalRank+=currRank;
                }
            }
            
        }
        if(totalRank ==-1){
            return -1;
        }
        return (double)totalRank/(double)count;
    }
    
    public int getTotalBirthRankedHigher(int year, String name , String gender){
        int rank = getRank(year,name,gender);
        int boysRank=0;
        int girlsRank=0;
        int totalBirthRankedHigherM=0;
        int totalBirthRankedHigherF=0;
        String path= "us_babynames_by_year/yob" + Integer.toString(year)+".csv";
        //String path = "testing/yob"+Integer.toString(year) + "short.csv";
        CSVParser parser = getParser(path);
        for(CSVRecord rec: parser){
            String currGender = rec.get(1);
            if(currGender.equals("M")){
                boysRank++;
            }
            if(currGender.equals("F")){
                girlsRank++;
            }
            if(gender.equals("F")&& girlsRank >= rank){
                break;
            }
            if(gender.equals("M")&& boysRank>=rank){
                break;
            }
            if(currGender.equals("M")&&gender.equals("M")){
                totalBirthRankedHigherM += Integer.parseInt(rec.get(2));
            }
            
            if(currGender.equals("F")&&gender.equals("F")){
                totalBirthRankedHigherF += Integer.parseInt(rec.get(2));
            }
        }
        if(gender.equals("M")){
            return totalBirthRankedHigherM;
        }
        
        return totalBirthRankedHigherF;
    }
    
    
    public void testGetTotalBirthRankedHigher(){
        int year = 1990;
        String name = "Drew";
        String gender = "M";
        int birthHigher= getTotalBirthRankedHigher(year, name , gender);
        System.out.println("no of birth who is ranked higher than " + name + " is "+ birthHigher);
    }
    
    
    public void testGetAverageRank(){
        String name = "Robert";
        String gender = "M";
        double averageRank = getAverageRank(name, gender);
        System.out.println("average rank of "+ name + " is " + averageRank);
    }
    
    public void testYearOfHighestRank(){
        String name="Mich";
        String gender="M";
        int year =yearOfHighestRank(name,gender);
        System.out.println("year of highest rank is " +year);
    }
    
    
    public void testWhatIsYourNameInYear(){
        String name="Owen";
        int year=1974;
        int newYear=2014;
        String gender="M"; 
        String newName= whatIsYourNameInYear(name,year,newYear,gender);
        System.out.println(name +" born in year " +year+" would be "+newName+" if born in year " +newYear);
    }
    
    //public void abc(){
      //  int year =2012;
      //  System.out.println("us_babynames_by_year/" + Integer.toString(year)+".csv");
    //}
    
    public void testTotalBirthInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser(false);
        totalBirthInFile(parser);
    }
    
    public void testGetRank(){
        int year= 1971;
        String name="Frank";
        String gender="M";
        int rank=getRank(year, name, gender);
        System.out.println(name+" rank is "+rank);
    }
    
    public void testGetName(){
        int year= 1982;
        String gender="M";
        int rank =450;
        String name=getName(year, rank, gender);
        System.out.println(rank+" corresponds to "+name+" in year "+year);
    }
}
