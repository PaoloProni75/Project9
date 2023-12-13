package it.unibz.andreypaolo;

public class DataProviderFactory {
    /*
    When true, the test uses static data (it is faster and no bandwidth used),
    when false, the test connects to the Open Data Hub real services
    */
    private final static boolean localTestData = true;

    public static DataProviderApi createProvider() {
        DataProviderApi dataProvider;
        if (localTestData)
            dataProvider = new OpenDataHubMock();
        else
            dataProvider = new OpenDataHub();
        return dataProvider;
    }


}
