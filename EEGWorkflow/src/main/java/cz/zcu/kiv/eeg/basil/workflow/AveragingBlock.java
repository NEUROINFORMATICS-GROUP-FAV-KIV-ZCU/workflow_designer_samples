package cz.zcu.kiv.eeg.basil.workflow;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Average a list of epochs using one stimuli marker
 * @author lvareka
 *
 */
@BlockType(type="AveragingBlock",family = "Preprocessing")
public class AveragingBlock implements Serializable {

	@BlockInput(name = "Markers",type="EEGMarker[]")
	private List<EEGMarker> markers;

	@BlockInput(name = "EEGData", type = "EEGDataList")
	private EEGDataPackageList epochs;

	@BlockOutput(name = "EEGData", type = "EEGDataList")
	private EEGDataPackageList eegData;

	public AveragingBlock(){
		//Required Empty Default Constructor for Workflow Designer
	}

	@BlockExecute
    public void process(){
	    eegData=new EEGDataPackageList(Arrays.asList(average(epochs.getEegDataPackage())));
    }

	public EEGDataPackage average(List<EEGDataPackage> epochs) {
		if (epochs == null || epochs.size() == 0 || this.markers == null)
			return null;
		double[][] firstEpoch = epochs.get(0).getData();
		double[][] average = new double[firstEpoch.length][firstEpoch[0].length];
		
		
		// sum of all related epochs
		int numberOfEpochs = 0;
		for (EEGDataPackage epoch: epochs) {
			for (EEGMarker marker: this.markers) {
				if (marker.getName().equals(epoch.getMarkers().get(0).getName())) {
					double[][] currData = epoch.getData();
					for (int i = 0; i < average.length; i++) {
						for (int j = 0; j < average[i].length; j++) {
							average[i][j] += currData[i][j];
						}
					}
					numberOfEpochs++;
				}
			}
		}
		
		// average by dividing 
		for (int i = 0; i < average.length; i++) {
			for (int j = 0; j < average[i].length; j++) {
				average[i][j] = average[i][j] / numberOfEpochs;
			}
		}
        return new EEGDataPackage(average, markers, epochs.get(0).getChannelNames(),epochs.get(0).getConfiguration());
	}

    public List<EEGMarker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<EEGMarker> markers) {
        this.markers = markers;
    }

    public EEGDataPackageList getEpochs() {
        return epochs;
    }

    public void setEpochs(EEGDataPackageList epochs) {
        this.epochs = epochs;
    }

    public EEGDataPackageList getEegData() {
        return eegData;
    }

    public void setEegData(EEGDataPackageList eegData) {
        this.eegData = eegData;
    }
}
