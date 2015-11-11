package cannon;

/**
 * Description: This sample demonstrate how to create a collection object in
 * DocuShare
 *
 * Copyright © 1996-2007 Xerox Corporation. All Rights Reserved.
 *
 * Author: Darby Cacdac
 *
 * This code is provided without a warranty of any kind. This example does not
 * constitute a patch, release, or software problem fix. The use of this code is
 * for sample usage and reference only.
 */

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.xerox.docushare.DSAuthenticationException;
import com.xerox.docushare.DSAuthorizationException;
import com.xerox.docushare.DSClass;
import com.xerox.docushare.DSException;
import com.xerox.docushare.DSFactory;
import com.xerox.docushare.DSHandle;
import com.xerox.docushare.DSInvalidLicenseException;
import com.xerox.docushare.DSSelectSet;
import com.xerox.docushare.DSServer;
import com.xerox.docushare.DSSession;
import com.xerox.docushare.object.DSCollection;
import com.xerox.docushare.property.DSLinkDesc;
import com.xerox.docushare.property.DSProperties;

public class Cannon {

    public static final String progName = "DSCreateColl";

    private DSSession dssession;
    private DSServer dsserver;

    private static String host = "localhost"; // DS HostName
    //private static int rmiPort = 1099; // rmi port
    private static String userName = "admin"; // userName
    private static String password = "2014#C@nn0n"; // password
    private static String domain = "DocuShare"; // User domain

    private String collTitle = "fredcoll";
    private String dest = "Collection-12339";

    @SuppressWarnings( "static-access" )
    // Method to process/parse the command line argument
//    private void processArgs( String[] args ) {
//
//        Options options = new Options();
//
//        try {
//            // Make username a required option
//            Option userName = OptionBuilder.withArgName( "username" )
//                    .hasArg()
//                    .withDescription( "Username to login as." )
//                    .isRequired()
//                    .create( "u" );
//
//            // Make password a required option
//            Option password = OptionBuilder.withArgName( "password" )
//                    .hasArg()
//                    .withDescription( "User password" )
//                    .isRequired()
//                    .create( "p" );
//
//            Option host = OptionBuilder.withArgName( "host" )
//                    .hasArg()
//                    .withDescription( "DocuShare host" )
//                    .create( "h" );
//
//            Option rmiPort = OptionBuilder.withArgName( "rmiport" )
//                    .hasArg()
//                    .withDescription( "DS host RMI port" )
//                    .create( "port" );
//
//            Option domain = OptionBuilder.withArgName( "domain" )
//                    .hasArg()
//                    .withDescription( "DocuShare domain" )
//                    .create( "d" );
//
//            Option collTitle = OptionBuilder.withArgName( "title" )
//                    .hasArg()
//                    .isRequired()
//                    .withDescription( "Title of the Collection to be created." )
//                    .create( "t" );
//
//            Option dest = OptionBuilder.withArgName( "collection_handle" )
//                    .hasArg()
//                    .isRequired()
//                    .withDescription(
//                            "Handle of the collection where you want to create the Collection." )
//                    .create( "dest" );
//
//            options.addOption( userName );
//            options.addOption( password );
//            options.addOption( host );
//            options.addOption( rmiPort );
//            options.addOption( domain );
//
//            options.addOption( collTitle );
//            options.addOption( dest );
//
//            // Parse the command line
//            CommandLineParser parser = new GnuParser();
//            CommandLine cmd = parser.parse( options, args );
//
//            // Check options
//            if ( cmd.hasOption( "u" ) ) {
//                this.userName = cmd.getOptionValue( "u" );
//            }
//            if ( cmd.hasOption( "p" ) ) {
//                this.password = cmd.getOptionValue( "p" );
//            }
//            if ( cmd.hasOption( "h" ) ) {
//                this.host = cmd.getOptionValue( "h" );
//            }
//            if ( cmd.hasOption( "port" ) ) {
//                this.rmiPort = Integer.parseInt( cmd.getOptionValue( "port" ) );
//            }
//            if ( cmd.hasOption( "d" ) ) {
//                this.domain = cmd.getOptionValue( "d" );
//            }
//            if ( cmd.hasOption( "t" ) ) {
//                this.collTitle = cmd.getOptionValue( "t" );
//            }
//            if ( cmd.hasOption( "dest" ) ) {
//                this.dest = cmd.getOptionValue( "dest" );
//            }
//
//        } catch ( NumberFormatException e ) {
//            System.out.println( "The RMI port must be an integer." );
//            System.exit( -1 );
//        } catch ( Exception e ) {
//            // automatically generate the help statement
//            HelpFormatter formatter = new HelpFormatter();
//            formatter.printHelp( progName, options, true );
//            // e.printStackTrace();
//            System.exit( -1 );
//        }
//
//    }

    // Login Routine
    private boolean dsLogin() {
        try {
            System.out.println( "Connecting to " + host + ":"  );
            dsserver = DSFactory.createServer( host );
            System.out.print( "Logging in to " + domain + " as user "
                    + userName + ".... " );
            dssession = dsserver.createSession( domain, userName, password );
            System.out.println( "Logged in!\n" );
            return true;

        } catch ( DSInvalidLicenseException e ) {
            System.out.println( "The DocuShare server does not have the require license." );
            return false;
        } catch ( DSAuthenticationException e ) {
            System.out.println( "Failed!\n" );
            return false;
        } catch ( DSException e ) {
            e.printStackTrace();
            return false;
        }
    }

    // Create Collection
    public void doCreateCollection() {
        try {
            // Get the class of the object we want created
            DSClass objClass = dssession.getDSClass( DSCollection.classname );

            // Create a prototype
            DSProperties colProp = objClass.createPrototype();

            // Set the required properties
            colProp.setPropValue( "title", collTitle );

            // Set the parent collection
            DSCollection parent = (DSCollection) dssession.getObject(
                    new DSHandle( dest ), DSSelectSet.NO_PROPERTIES );

            // create the collection
            DSHandle newCollHnd = dssession.createObject( colProp,
                    DSLinkDesc.containment, parent, null, null );
            System.out.println( "Created " + newCollHnd + " (" + collTitle
                    + ") in " + parent );

        } catch ( DSInvalidLicenseException dse ) {
            System.out.println( "The DocuShare server does not have the require license." );
            System.exit( -1 );
        } catch ( DSAuthorizationException dse ) {
            System.out.println( "You do not have the proper permissions for this operation." );
            System.exit( -1 );
        } catch ( DSException dse ) {
            dse.printStackTrace();
            System.exit( -1 );
        }
    }

    public static void main( String[] args ) {

        Cannon test = new cannon.Cannon();
       // test.processArgs( args );

        // Login to the DocuShare host
        if ( test.dsLogin() ) {
            // create collection
            test.doCreateCollection();
        }
        System.exit( 0 );
    }
}