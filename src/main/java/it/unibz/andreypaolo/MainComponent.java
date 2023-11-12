package it.unibz.andreypaolo;

import it.unibz.andreypaolo.highlevel.Mobility;
import it.unibz.andreypaolo.highlevel.Tourism;
import it.unibz.andreypaolo.highlevel.ValueObject;
import it.unibz.andreypaolo.lowlevel.DataProviderApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainComponent {

    private DataProviderApi dataProvider;

    public MainComponent(DataProviderApi dataProvider) {
        this.dataProvider = dataProvider;
    }

    public String read(String mobilityApiUrl, String mobilityQueryField, String tourismApiUrl,
                       String tourismQueryField) throws IOException, InterruptedException {
        // What if the add another provider ? Why do we need the two classes Tourism and Mobility
        // Because of the rules about data formats and actually we do not search by a path.
        // Let's say the first is Mobility and the second is Tourism

        Mobility mobility = new Mobility(dataProvider);
        List<ValueObject> mobilityItems = mobility.readDataFromService(mobilityApiUrl, mobilityQueryField);

        Tourism tourism = new Tourism(dataProvider);
        List<ValueObject> tourismItems = tourism.readDataFromService(tourismApiUrl, tourismQueryField);

        List<ValueObject> resultList = new ArrayList<>(mobilityItems.size() + tourismItems.size());
        resultList.addAll(mobilityItems);
        resultList.addAll(tourismItems);

        Collections.sort(resultList);

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (ValueObject vo : resultList) {
            String body = vo.getBodyNodes().toString();
            sb.append('{').append(body).append("},\n");
        }
        sb.deleteCharAt(sb.length()-2);
        sb.append("]");

        return sb.toString();
    }
}
