package it.unibz.andreypaolo;

import it.unibz.andreypaolo.lowlevel.DataProviderApi;
import it.unibz.andreypaolo.lowlevel.OpenDataHub;
import it.unibz.andreypaolo.lowlevel.OpenDataHubMock;

public class DataProviderFactory {
    /*
    When true, the test uses static data (it is faster and no bandwidth used),
    when false, the test connects to the Open Data Hub real services
    */
    private final static boolean mock = true;

    public static DataProviderApi createProvider() {
        DataProviderApi dataProvider;
        if (mock)
            dataProvider = new OpenDataHubMock();
        else
            dataProvider = new OpenDataHub();
        return dataProvider;
    }


}
