package com.sanzfdu.cafeteriaetsib.bl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sanzfdu.cafeteriaetsib.R;
import com.sanzfdu.cafeteriaetsib.dl.Bocata;
import com.sanzfdu.cafeteriaetsib.dl.Ingrediente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MirenPablo on 11/06/2015.
 */
public class MySQL extends SQLiteOpenHelper implements InterfaceCallAPI{

    private List<Bocata> lbagg = new ArrayList<Bocata>();
    private List<Ingrediente> lingr;
    private Context context;
    private SQLiteDatabase db;
    private boolean comprUpgrade=false;
    public static boolean needsUpgrade=false;

    //QUE CAMPOS TIENE LA DB???
    String sqlC = "CREATE TABLE ";
    String sqlD = "DROP TABLE ";
    String sqlAdd = "INSERT INTO ";

    public MySQL(Context context,String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        this.context=context;

    }

    /*Cuando se crea un elemento de esta clase MySQL se comprueba si la database existe. Si es asi,
     no hace el OnCreate. En caso de que no exista, con el contexto de aplicacion que se le
      ha pasado en el constructor, creara la database y despues creara
      las tablas en base al string que le pasamos en el execSQL del OnCreate*/

    /*NOTA: AL hacerlo con MySQL workbench los nombres de las tablas son del estilo de " `bocatasUni`.`Bocatas` "
    * pero parece que para SQLite con poner solo el nombre de la tabla vale. Supongo que esto es porque la propia
    * db se crea con el constructor de la clase, con el que esta arriba*/
    @Override
    public void onCreate(SQLiteDatabase db){

        /*NOTA VITAL:: SI PONEMOS IF NOT EXIST en la tabla, esto puede cascar porque podrian quedar
        bases de datos antiguas con distintos campos de tabla, y eso causaria problemas
        porque luego intentemos meter, sacar... datos de campos inexistentes. Asi que OJO!*/

        /*NOTA: Los campos ya no seran varchar y tal como en MySQL, solo son TEXT,REAL,INTEGER
        y unos pocos tipos mas, asi que... ni intentarlo, tiene que ser algo asi*/

        db.execSQL(sqlC.concat("Bocatas (\n" +
                "  Nombre TEXT NOT NULL PRIMARY KEY,\n" +
                "  Precio REAL NOT NULL,\n" +
                "  Rate REAL NOT NULL,\n" +
                "  Fav REAL NOT NULL,\n" +
                "  Antiguedad INTEGER NOT NULL\n);"));
        db.execSQL(sqlC.concat("Ingredientes (\n" +
                "  Nombre TEXT NOT NULL PRIMARY KEY\n);" +
                "\n"));
        db.execSQL(sqlC.concat("Bocata_has_ingrediente (\n" +
                        "  Bocatas_Nombre TEXT NOT NULL,\n" +
                        "  Ingredientes_Nombre TEXT NOT NULL,\n" +
                        "  CONSTRAINT fk_Bocata_has_ingrediente_Bocatas\n" +
                        "    FOREIGN KEY (Bocatas_Nombre)\n" +
                        "    REFERENCES Bocatas (Nombre)\n" +
                        //en ON DELETE "CASCADE" para evitar incongruencias
                        "    ON DELETE CASCADE\n" +
                        "    ON UPDATE NO ACTION,\n" +
                        "  CONSTRAINT fk_Bocata_has_ingrediente_Ingredientes\n" +
                        "    FOREIGN KEY (Ingredientes_Nombre)\n" +
                        "    REFERENCES Ingredientes (Nombre)\n" +
                        "    ON DELETE CASCADE\n" +
                        "    ON UPDATE NO ACTION);\n"));
        this.db=db;
        CallAPI callAPI = new CallAPI(this);
        callAPI.execute(context.getResources().getString(R.string.URL_database));
    }

    /*NOTA: el siguiente comentario no es importante porque no habia ninguno en este caso, pero es interesante,
    Aqui el INDEX normal si puede meterse en el "CREATE TABLE"*/
    /*LOS CREATE INDEXES DE LAS FOREIGN KEY (fk) TIENEN QUE IR AL FINAL Y FUERA DEL
    "CREATE TABLE" PORQUE SI NO CASCA!! Esto es asi porque CREATE INDEX ya es una sentencia propia,
    por lo que no puede ir dentro de la sentencia CREATE TABLE
    Importante el ";" si queremos distinguir la creacion de tabla
    de la creacion de los indexes de las foreign key*/

    //Cuando la version de la base de datos cambia, actualiza lo que le decimos
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        needsUpgrade=true;
        System.out.println("Necesito descargar la nueva version");
        obtainBaggs(db);
        db.execSQL(sqlD.concat("Bocatas"));
        db.execSQL(sqlD.concat("Ingredientes"));
        db.execSQL(sqlD.concat("Bocata_has_ingrediente"));
        comprUpgrade=true;
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //en este caso no queremos que haga nada, lo metemos para que si viene una version vieja
        //no nos rompa las cosas, nada mas
    }
    //Para guardar los favs ya introducidos al borrar la vieja db
    public void obtainBaggs(SQLiteDatabase db){
        Cursor cBagg = null;
        Bocata bocata;
        cBagg=db.rawQuery("SELECT * FROM Bocatas",null);
       if(cBagg.moveToFirst()) {
           do {
               bocata = new Bocata(cBagg.getString(0),cBagg.getFloat(1),cBagg.getFloat(2),cBagg.getFloat(3),cBagg.getInt(4),null);
               lbagg.add(bocata);
           } while (cBagg.moveToNext());
       }
    }

    private void fillDatabase(SQLiteDatabase db,List<Bocata> lBocata){
        List<Bocata>lBoc=new ArrayList<Bocata>();
        String addStr="";
        /*No se puede usar el INSERT INTO bocatas VALUES (campo1,campo2,...),(campo1,campo2,...),
        (campo1,campo2,...);*/
        //FORMATO DE SQLite (En versiones viejas esto, en las nuevas se puede hacer como en MySQL pero...)
       /* db.execSQL(sqlAdd.concat("'Bocatas' SELECT " +
                        "'Bacon_Queso' as 'Nombre',3.0 AS Precio,0.0 AS Rate,2.5 AS Fav,0 AS Antiguedad " +
                "UNION SELECT 'Caida',3.6,0.0,4.0,0 " +
                "UNION SELECT 'Calamares',2.45,0.0,0.0,0 "));
        */
        if(comprUpgrade)
            lBoc=mantenerFav(lbagg, lBocata);//para que si tenia un fav para un bocadillo no se pierda, se "añade" al listado actual
        //Lo siguiente lo puedo hacer porque se el orden de los campos y todo, si no no podria hacerlo
        //asi
        else
            lBoc=lBocata;

        for(int i = 0; i < lBoc.size();i++) {
            if (i == 0) {
                //Lo siguiente lo puedo hacer porque se el orden de los campos y todo, si no no podria hacerlo
                //asi
                addStr = (sqlAdd.concat("'Bocatas' SELECT " +
                        "'"+lBoc.get(i).getNombre()+"' as 'Nombre',"+lBoc.get(i).getPrecio()+" AS Precio," +
                        ""+lBoc.get(i).getRate()+" AS Rate,"+lBoc.get(i).getFav()+" AS Fav,"+lBoc.get(i).getAntiguedad()+" " +
                        "AS Antiguedad "));//Esto ya va bien???
                //System.out.println("Bocadillo 0 metido");
            } else {
                addStr = addStr +  "UNION SELECT '"+lBoc.get(i).getNombre()+"'," +
                        ""+lBoc.get(i).getPrecio()+","+lBoc.get(i).getRate()+"," +
                        ""+lBoc.get(i).getFav()+"," +
                        ""+lBoc.get(i).getAntiguedad()+" ";
                //System.out.println("Bocadillo "+i+" metido");
            }
            //System.out.println("Lo que estoy metiendo en bocatas es"+addStr+"\n");
        }
        //si haces execSQL con una lista null entonces se devuelve el error -> SQLiteLog: (21) API called with NULL prepared statement
        db.execSQL(addStr);//Es vital ponerle el execSQL para que lo ejecute y todo se meta a db, si no no funcionaria!
        addStr = "";
        List<String>ingrs = fillIngrList(lBoc);
        for(int i = 0; i < ingrs.size();i++){
            if(i == 0) {
                addStr = (sqlAdd.concat(" 'Ingredientes' SELECT '" +
                        ingrs.get(i)+"' AS 'Nombre' "));
            }else{
                addStr = addStr +  "UNION SELECT '"+ingrs.get(i)+"' ";
            }
            //System.out.println("Lo que estoy metiendo en ingredientes es"+addStr+"\n");
        }
        db.execSQL(addStr);
        addStr = "";
        List<String>bagghasingr = fillBaggHasIngrList(lBoc);
        for(int i = 0; i < bagghasingr.size();i++) {
            if (i == 0) {
                //Lo siguiente lo puedo hacer porque se el orden de los campos y todo, si no no podria hacerlo
                //asi
                addStr = (sqlAdd.concat(" 'Bocata_has_ingrediente' SELECT '" +
                        bagghasingr.get(i)+"' AS 'Bocatas_Nombre','"+bagghasingr.get(++i)+"' AS 'Ingredientes_Nombre' "));
            } else {
                addStr = addStr +  "UNION SELECT '"+bagghasingr.get(i)+"','"+bagghasingr.get(++i)+"' ";
            }
            //System.out.println("Lo que estoy metiendo en el hash es" + addStr + "\n");
        }
        comprUpgrade=false;
        db.execSQL(addStr);         //NOTA: Cuidado no este dentro del for, si no estamos metiendo varias veces los mismos elementos en la db
        db.close();
        ListOfThings lof = new ListOfThings();
        lof.fillLists(context);

    }

    public List<Bocata> extractData(Cursor cBoc, SQLiteDatabase db){
        Bocata bocata;
        Cursor cBocHasIngr = null;
        lbagg = new ArrayList<Bocata>();
        do{

            /*NOTA: Es importante saber que para insertar,hacer las busquedas,... con variables debemos
            poner despues del "WHERE campo=" un ' antes y despues de la variable de tipo
            String que queremos insertar, si no no funcionara porque en la db estan "escritos" asi*/
            cBocHasIngr = db.rawQuery("SELECT Ingredientes_Nombre FROM Bocata_has_ingrediente WHERE Bocatas_Nombre='"+cBoc.getString(0)+"'",null);
            buscaIngredientes(cBocHasIngr, db);
            //objeto obj = new objeto(elem1,elem2,elem3...);
            bocata = new Bocata(cBoc.getString(0),cBoc.getFloat(1),cBoc.getFloat(2),cBoc.getFloat(3),cBoc.getInt(4),lingr);

            //lista.add(obj);
            lbagg.add(bocata);
        }while(cBoc.moveToNext());//Mientras haya algo despues sigue haciendolo, cuando lo siguiente es null o no hay nada, para de leer
        cBocHasIngr.close();
        return lbagg;
    }
    private void buscaIngredientes(Cursor c, SQLiteDatabase db){
        Cursor cIngr =null;
        Ingrediente ingred;
        lingr = new ArrayList<Ingrediente>();

        if(c.moveToFirst()) {

            do {
                cIngr = db.rawQuery("SELECT * FROM Ingredientes WHERE Nombre='" + c.getString(0) + "'", null);
                System.out.println(" "+c.getString(0));
                if (cIngr.moveToFirst()) {
                    ingred = new Ingrediente(cIngr.getString(0), null);
                    lingr.add(ingred);
                }
            } while (c.moveToNext());
            cIngr.close();
        }
        else{
            lingr = null;
        }
    }

    public static String createQuery(List<String> s,String tableName,String param,String cond_ext) {
        //OJO!! Acordarse que ='variable' funciona pero =' variable ' NO porque buscaria con los espacios y todo
        String query = "SELECT * FROM " + tableName + " WHERE "+ param +" ='";

        if (s.size() < 2) {
            query = query + s.get(0)+"'";
        } else {
            //Para hacer la peticion en base a las cosas que me pidan
            for (int i = 0; i < s.size(); i++) {
                query = query + s.get(i)+"' ";
                if (i < s.size() - 1) {
                    //TIENE QUE SER OR, con AND elegiria mal los bocadillos (muchos nombres como condicion)
                    //Y ademas en el caso de la tabla de HA
                    // SH no podria encontrar los bocadillos, porque
                    //la relacion en esta tabla es 1 to 1
                    query = query +" "+cond_ext+" "+param+" ='";
                }
            }
        }


        return query;
    }

    @SuppressWarnings("unchecked")
    public void parseCallResponse(JSONObject json) {
        List<Bocata> lBocata=new ArrayList<Bocata>();
        try {
            JSONArray arrayJBoc = json.getJSONArray("results");
            lBocata=JSONParser.parseDatabase(arrayJBoc);
            if(lBocata.size()>0) {
                fillDatabase(db,lBocata);
            }
        } catch (JSONException e) {

        }
    }

   public List<Bocata> mantenerFav(List<Bocata> lbagg, List<Bocata> lboc){

       for(int i=0;i<lboc.size();i++)
       {
           for(int j=0;j<lbagg.size();j++){
               if(lbagg.get(j).getNombre().equals(lboc.get(i).getNombre())){//NO SE PUEDE PONER STRING==STRING, es CON EQUALS!
                   lboc.get(i).setFav(lbagg.get(j).getFav());
                   System.out.println("Bocadillo"+lboc.get(i).getNombre()+"con fav"+lboc.get(i).getFav()+"localizado");
                   j=lbagg.size();
               }
           }

       }
       //System.out.println("Estoy acabando de rellenar los bocadillos fav");
       return lboc;
   }

    public List<String>fillIngrList(List<Bocata>lBoc){
        List<String>lIngrString=new ArrayList<String>();
        for(int i=0;i<lBoc.size();i++) {
            for (int j = 0; j < lBoc.get(i).getIngredientes().size(); j++) {
                if (!lIngrString.contains(lBoc.get(i).getIngredientes().get(j).getNombre())) {//solo si no estan ya annadidos
                    lIngrString.add(lBoc.get(i).getIngredientes().get(j).getNombre());//Guarda ingrediente a ingrediente
                    //System.out.println(lIngrString.get(j));
                }
            }
        }
        return lIngrString;
    }

    public List<String>fillBaggHasIngrList(List<Bocata>lBoc)
    {
        List<String>lBaggHasIngrString=new ArrayList<String>();

        for(int i=0;i<lBoc.size();i++) {
            //System.out.println("Lo que estoy metiendo en ingredientes es"+ingrs+"\n");
            for (int j = 0; j < lBoc.get(i).getIngredientes().size(); j++) {
                lBaggHasIngrString.add(lBoc.get(i).getNombre());
                lBaggHasIngrString.add(lBoc.get(i).getIngredientes().get(j).getNombre());
            }
            // System.out.println("Lo que estoy metiendo en hash es"+baggHasIngr+"\n");
        }
        return lBaggHasIngrString;
    }

}
