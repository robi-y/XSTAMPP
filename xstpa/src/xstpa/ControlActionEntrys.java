package xstpa;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ControlActionEntrys {
	
	private String controller;
	private String controlAction;
	private Boolean safety_critical;
	private String comments;
	
	private UUID id;
	private int number;

	private List<ProcessModelVariables>  linkedItems = new ArrayList<ProcessModelVariables>();

	private List<ProcessModelVariables>  availableItems = new ArrayList<ProcessModelVariables>();
	
	// all combinations generated by ACTS

	private List<ProcessModelVariables>  contextTableCombinations = new ArrayList<ProcessModelVariables>();
	// not provided Control Action available Items
	private List<ProcessModelVariables>  npavailableItems = new ArrayList<ProcessModelVariables>();
	
	public ControlActionEntrys() {
		comments = "";
		safety_critical = true;
		
	}
	
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}
	public String getControlAction() {
		return controlAction;
	}
	public void setControlAction(String controlAction) {
		this.controlAction = controlAction;
	}
	public Boolean getSafetyCritical() {
		return safety_critical;
	}
	public void setSafetyCritical(Boolean safety_critical) {
		this.safety_critical = safety_critical;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<ProcessModelVariables> getLinkedItems() {
		return linkedItems;
	}

	public void setLinkedItems(List<ProcessModelVariables> linkedItems) {
		this.linkedItems = linkedItems;
	}
	
	public void addLinkedItem(ProcessModelVariables item) {
		linkedItems.add(item);
	}
	
	
	public void removeLinkedItem(int id) {
		linkedItems.remove(id);
	}
	
	public void removeLinkedItem(ProcessModelVariables item) {
		linkedItems.remove(item);
		
	}
	
	public void removeAllLinkedItems() {
		linkedItems.clear();
	}
	
	public ProcessModelVariables getLinkedItem(int id) {
		return linkedItems.get(id-1);
	}


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<ProcessModelVariables> getAvailableItems() {
		return availableItems;
	}

	public void setAvailableItems(List<ProcessModelVariables> availableItems) {
		this.availableItems = availableItems;
	}
	
	public void addAvailableItem(ProcessModelVariables item) {
		availableItems.add(item);
		//setNumber(availableItems.indexOf(item)+1);
		//setNumber(item.getNumber());
	}
	
	public void addAvailableItem(int position, ProcessModelVariables item) {
		availableItems.set(position, item);
	}
	
	public void removeAvailableItem(int id) {
		availableItems.remove(id);
	}
	
	public void removeAvailableItem(ProcessModelVariables item) {
		availableItems.remove(item);
		
	}
	
	public void removeAllAvailableItems() {
		availableItems.clear();
		
	}
	
	/**
	 * This Function sorts a given List by its ID (Number)
	 * @param list
	 * 		the list to be sorted
	 * @return list
	 * 		the sorted list
	 */
	public List<ProcessModelVariables> sortItems(List<ProcessModelVariables> list) {
		
		ProcessModelVariables[] tempList = new ProcessModelVariables[linkedItems.size() + availableItems.size()];

		
		for (ProcessModelVariables entry : list) {
			if (entry.getNumber()-1 > tempList.length) {
				
			}
			tempList[(entry.getNumber())-1] = entry;
			}
		list.clear();
		for (ProcessModelVariables entry : tempList) {
			if (entry != null) {
				list.add(entry);
			}
		}
			
			
		
		
		return list;
		
	}
	
	public List<ProcessModelVariables> sortnpItems(List<ProcessModelVariables> list) {
		
		ProcessModelVariables[] tempList = new ProcessModelVariables[linkedItems.size() + availableItems.size()];

		
		for (ProcessModelVariables entry : list) {
			if (entry.getNumber()-1 > tempList.length) {
				
			}
			tempList[(entry.getNumber())-1] = entry;
			}
		list.clear();
		for (ProcessModelVariables entry : tempList) {
			if (entry != null) {
				list.add(entry);
			}
		}
			
			
		
		
		return list;
		
	}

	public List<ProcessModelVariables> getNpavailableItems() {
		return npavailableItems;
	}

	public void setNpavailableItems(List<ProcessModelVariables> npavailableItems) {
		this.npavailableItems = npavailableItems;
	}
	@XmlElementWrapper(name = "contexttablecombinations")
	@XmlElement(name = "processmodelvariables")
	public List<ProcessModelVariables> getContextTableCombinations() {
		return contextTableCombinations;
	}

	public void setContextTableCombinations(List<ProcessModelVariables> contextTableCombinations) {
		this.contextTableCombinations = contextTableCombinations;
	}
	
	public void addContextTableCombination(ProcessModelVariables entry) {
		contextTableCombinations.add(entry);
	}

}
