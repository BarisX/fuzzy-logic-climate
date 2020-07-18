package fuzzylogic;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Rule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 15.10.2019 - 20:38
 * part of fuzzy
 *
 * @author user12043
 */
public class Clima {
    private final FIS fis;
    // inputlar
    private final double relativeCompactness;
    private final double surfaceArea;
    private final double wallArea;
    private final double roofArea;
    private final double overallHeight;
    private final double orientation;
    private final double glazingArea;
    private final double glazingAreaDistribution;
    private final double heatingLoad;
    private final double coolingLoad;
    

    public Clima(double relativeCompactness, 
                            double surfaceArea,
                            double wallArea,
                            double roofArea,
                            double overallHeight,
                            double orientation,
                            double glazingArea,
                            double glazingAreaDistribution,
                            double heatingLoad,
                            double coolingLoad) 
                            {
        this.relativeCompactness = relativeCompactness;
        this.surfaceArea = surfaceArea;
        this.wallArea = wallArea;
        this.roofArea = roofArea;
        this.overallHeight = overallHeight;
        this.orientation = orientation;
        this.glazingArea = glazingArea;
        this.glazingAreaDistribution = glazingAreaDistribution;
        this.heatingLoad = heatingLoad;
        this.coolingLoad = coolingLoad;
        fis = FIS.load("fcl/climate.fcl", true);
        fis.setVariable("Bağıl Kompaktlık", relativeCompactness);
        fis.setVariable("Yüzey Alanı", surfaceArea);
        fis.setVariable("Duvar Alanı", wallArea);
        fis.setVariable("Çatı Alanı", roofArea);
        fis.setVariable("Ortalama Yükseklik", overallHeight);
        fis.setVariable("Doğuya Doğru İnşa Etme", orientation);
        fis.setVariable("Cam Alanı ", glazingArea);
        fis.setVariable("Cam Alanı Dağılımı", glazingAreaDistribution);
        fis.setVariable("Sıcak Yüklemesi", heatingLoad);
        fis.setVariable("Soğuk Yüklemesi", coolingLoad);
        
        fis.evaluate();
    }

    static void process() {
        Clima clima = new Clima(0.62,808.5,367,220,3.5,3,0.25,5,13.99,14.61);
        System.out.println("-----------------------------------");
        System.out.println(clima);

        JFuzzyChart.get().chart(clima.getModel().getVariable("heatingLoad"), true);
        JFuzzyChart.get().chart(clima.getModel().getVariable("heatingLoad").getDefuzzifier(), "Sıcaklık Yüklemesi", true);

        // print rules
        System.out.println("Rules:");
        final List<Rule> rules = clima.getModel().getFunctionBlock("clima").getFuzzyRuleBlock("ruleBlock1").getRules();
        final List<Rule> workedRules = rules.stream().filter(rule -> rule.getDegreeOfSupport() > 0).collect(Collectors.toList());
        for (Rule rule : workedRules) {
            System.out.println(rule);
        }
    }

    private FIS getModel() {
        return fis;
    }

    @Override
    public String toString() {
        return "Diğer Etmenler: " + roofArea + "\nCam Genişliği: " + glazingArea
                + "\nSıcaklık Yüklemesi: " + Math.round(fis.getVariable("heatingLoad").getValue());
    }
}
