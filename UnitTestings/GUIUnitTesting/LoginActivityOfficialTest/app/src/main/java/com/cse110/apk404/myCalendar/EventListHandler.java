package com.cse110.apk404.myCalendar;

import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventListHandler {


	//list to store all static events
	private static StaticEventList staticList = null;

	//list to store all sorted final dynamic events
	private static DynamicEventList dynamicList = null;

	//list to store all unsorted dynamic events
	private static DynamicEventList deadlineList = null;

	//list to store all finished dynamic events (grey)
	private static DynamicEventList finishedDynamicList = null;

	private static ArrayList<CalendarEvent> events = null; //list to store all events in one given day
	private static int startTimeOfDay;
	private static int endTimeOfDay;


	public static ArrayList<CalendarEvent> getEvents() {
		return events;
	}

	public static void setEvents(ArrayList<CalendarEvent> events) {
		EventListHandler.events = events;
	}

	public static int getStartTimeOfDay() {
		return startTimeOfDay;
	}

	public static void setStartTimeOfDay(int startTimeOfDay) {
		EventListHandler.startTimeOfDay = startTimeOfDay;
	}

	public static int getEndTimeOfDay() {
		return endTimeOfDay;
	}

	public static void setEndTimeOfDay(int endTimeOfDay) {
		EventListHandler.endTimeOfDay = endTimeOfDay;
	}

	public static CalendarEvent getEventById(long Id) {

		ArrayList<StaticEvent> staticArrayList = staticList.getList();
		if (staticArrayList != null) {
			for (int i = 0; i < staticArrayList.size(); i++) {
				if (staticArrayList.get(i).getId() == Id) {
					return staticArrayList.get(i);
				}
			}
		}
		ArrayList<DynamicEvent> dynamicArrayList = dynamicList.getList();
		if (dynamicArrayList != null) {
			for (int i = 0; i < dynamicArrayList.size(); i++) {
				if (dynamicArrayList.get(i).getId() == Id) {
					return dynamicArrayList.get(i);
				}
			}
		}

		return null;
	}


	public static void initStaticList() {
		if (staticList == null)
			staticList = new StaticEventList();
	}
//
//	public static void initDynamicList(int start, int end) {
//		EventListHandler.setStartTimeOfDay(start);
//		EventListHandler.setEndTimeOfDay(end);
//		if (dynamicList == null)
//			dynamicList = new DynamicEventList();
//		if (deadlineList == null)
//			deadlineList = new DynamicEventList();
//		if (finishedDynamicList == null)
//		finishedDynamicList = new DynamicEventList();
//	}

	public static void initDynamicList() {
		Log.e("shit", "inside init");
		int start = 9;
		int end = 21;
		EventListHandler.setStartTimeOfDay(start);
		EventListHandler.setEndTimeOfDay(end);
		if (dynamicList == null)
			dynamicList = new DynamicEventList();
		if (deadlineList == null)
			deadlineList = new DynamicEventList();
		if (finishedDynamicList == null)
			finishedDynamicList = new DynamicEventList();
	}



	public static StaticEventList getStaticList() {
		return staticList;
	}

	public static void setStaticList(StaticEventList staticList) {
		EventListHandler.staticList = staticList;
	}

	public static DynamicEventList getDynamicList() {
		return dynamicList;
	}

	public static void setDynamicList(DynamicEventList dynamicList) {
		EventListHandler.dynamicList = dynamicList;
	}

	public static void setDeadlineList(DynamicEventList list) {
		EventListHandler.deadlineList = list;
	}

	public static void setFinishedDynamicList(DynamicEventList list) {
		EventListHandler.finishedDynamicList = list;
	}

	public static void clearAllLists() {
		Log.e("shit", "inside clear");
		EventListHandler.dynamicList.setList(new ArrayList<DynamicEvent>());
		EventListHandler.staticList.setList(new ArrayList<StaticEvent>());
		EventListHandler.deadlineList.setList(new ArrayList<DynamicEvent>());
		EventListHandler.dynamicList = null;
		EventListHandler.staticList = null;
		EventListHandler.deadlineList = null;
	}


	public static DynamicEventList getDeadlineList() {
		return EventListHandler.deadlineList;
	}

	public static DynamicEventList getFinishedDynamicList() {
		return EventListHandler.finishedDynamicList;
	}
	public static boolean checkValidTime(Calendar startTime, Calendar endTime) {
		if (startTime.compareTo(endTime) >= 0) {
			return false;
		}
		return true;
	}

	//Create a static event to add to the static event list
	public static boolean createStaticEvent(String name, String location, Calendar startTime, Calendar endTime,
			boolean isStatic, boolean isPeriodic, boolean isFinished, String description, String color) throws CalendarError {
		//check if start and end times are valid
		boolean check = false;
		if (!checkValidTime(startTime, endTime)) {
			return false;
		}
		if(startTime.get(Calendar.DAY_OF_MONTH) != endTime.get(Calendar.DAY_OF_MONTH))
			return false;
		//check if event is static
		if (isStatic == false)
			return false;

		// Set time seconds to 0 to avoid bugs
		startTime.set(Calendar.SECOND, 0);
		endTime.set(Calendar.SECOND, 0);

		StaticEvent staticEvent = new StaticEvent(name, location, startTime, endTime, isStatic,
				isPeriodic, isFinished, description, color);
		staticEvent.setId(System.currentTimeMillis());
		check = staticList.addEvent(staticEvent);

		return (check);
	}


	public static boolean removeEventById(long temp) throws CalendarError {
		boolean check = true;
		if (staticList == null) {
			check = false;
		}
		if (dynamicList == null) {
			check = false;
		}
		if(check == false)
			return false;
		check = staticList.removeEventById(temp);
		check = dynamicList.removeEventById(temp);
		check = deadlineList.removeEventById(temp);

		return check;
	}



	//Dynamic sort algorithm
	public static boolean dynamicSort(DynamicEvent dynamicEvent) throws CalendarError {

		Comparator<StaticEvent> staticcomparator = new Comparator<StaticEvent>() {

			@Override
			public int compare(StaticEvent o1, StaticEvent o2) {
				return o1.getStartTime().compareTo(o2.getStartTime());
			}
		};

		Comparator<DynamicEvent> comparator = new Comparator<DynamicEvent>() {

			@Override
			public int compare(DynamicEvent o1, DynamicEvent o2) {
				return o1.getDeadline().compareTo(o2.getDeadline());
			}
		};


		PriorityQueue<DynamicEvent> currDynamicEList = new PriorityQueue<DynamicEvent>(1,comparator);
		PriorityQueue<StaticEvent> currStaticEList = new PriorityQueue<StaticEvent>(1,staticcomparator);
		PriorityQueue<StaticEvent> sortedStaticEList = new PriorityQueue<StaticEvent>(1,staticcomparator);
		PriorityQueue<StaticEvent> freeList = new PriorityQueue<StaticEvent>(1,  staticcomparator);
		PriorityQueue<StaticEvent> sortedfreeList = new PriorityQueue<StaticEvent>(1, staticcomparator);

		ArrayList<StaticEvent> staticArrayList = staticList.getList();
		ArrayList<DynamicEvent> dynamicArrayList = null;
		ArrayList<DynamicEvent> finishedDynamicEvents = null;


		if (dynamicList != null){
			finishedDynamicEvents = dynamicList.getList();
		}


		if (deadlineList != null) {
			dynamicArrayList = deadlineList.getList();
		}

		if (staticArrayList != null) {
			for (int i = 0; i < staticArrayList.size(); i++) {
				if (!staticArrayList.get(i).isFinished()) {
					currStaticEList.add(staticArrayList.get(i));
				}
			}
		}

//		Calendar cal = Calendar.getInstance();
		if (dynamicArrayList != null) {
			for (int i = 0; i < dynamicArrayList.size(); i++) {
				if (!dynamicArrayList.get(i).isFinished()) {
//					if(dynamicArrayList.get(i).getStartTime() != null && dynamicArrayList.get(i).getStartTime().compareTo(cal) <= 0)
//						dynamicArrayList.get(i).setFinished(true);
					currDynamicEList.add(dynamicArrayList.get(i));
				}

			}
		}

		Calendar cal2 = Calendar.getInstance();
		if (finishedDynamicEvents != null) {
			for (int i = 0; i < finishedDynamicEvents.size(); i++) {

//					if(finishedDynamicEvents.get(i).getStartTime() != null && finishedDynamicEvents.get(i).getStartTime().compareTo(cal2) <= 0) {
//						System.out.println("Set Finished!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + finishedDynamicEvents.get(i).getStartTime().get(Calendar.HOUR_OF_DAY) + ":" +
//								finishedDynamicEvents.get(i).getStartTime().get(Calendar.MINUTE) );
//						finishedDynamicEvents.get(i).setFinished(true);
//					}

				if(finishedDynamicEvents.get(i).isFinished())
					finishedDynamicList.addEvent(finishedDynamicEvents.get(i));

			}
		}


		DynamicEvent test = null;
		Iterator<DynamicEvent> iter = currDynamicEList.iterator();
		while(iter.hasNext())
		{
			test = iter.next();
			Date start = test.getDeadline().getTime();
			DateFormat time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			System.out.println("DynamicEList deadline: "+time.format(start));
		}


		StaticEvent se;


		EventListHandler.checkConflict(currStaticEList, sortedStaticEList);

		EventListHandler.purgeStaticList(sortedStaticEList);

		EventListHandler.updateFreeTime(sortedStaticEList, currDynamicEList, freeList, sortedfreeList);
		//System.out.println("1111111111");
		PriorityQueue<StaticEvent> newsortedfreeList = new PriorityQueue<StaticEvent>(1,  staticcomparator);

		EventListHandler.purgefreeTime(sortedfreeList, newsortedfreeList);



		//		while(!sortedfreeList.isEmpty()) {
		//			DateFormat time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		//			event = sortedfreeList.poll();
		//			Date start1 = event.getStartTime().getTime();
		//			Date end1 = event.getEndTime().getTime();
		//			System.out.println("freeList: ");
		//			System.out.println(time.format(start1));
		//			System.out.println(time.format(end1));
		//
		//		}

		Iterator<StaticEvent> it = newsortedfreeList.iterator();

		while(it.hasNext()){
			se = it.next();
			Date start = se.getStartTime().getTime();
			Date end = se.getEndTime().getTime();
			DateFormat time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			System.out.println("Srt: " + time.format(start));
			System.out.println("End: " + time.format(end));
		}


		dynamicList.getList().clear();
		boolean retval = EventListHandler.dynamicAllocation(newsortedfreeList, currDynamicEList);
		if(retval == false)
			System.out.println("Dynamic Allocation error");


        if(dynamicList != null) {
            Log.e("shit", "" + dynamicList.getList().size());
            boolean toCheck = dynamicList.checkEndtimeDeadline();
            if (toCheck == false)
                return false;
        }


		return retval;
	}

	//front end passes in fields to create a dynamic event, and we put this event into the priority queue
	public static boolean createDynamicEvent(String name, boolean isStatic, String location, String description, String color,
			Calendar deadline, int estimatedLength, boolean isFinished) throws CalendarError {

		boolean check = false;
		if (isStatic == true)
			return false;

		// check if (current + estimated time length) < deadline time, return false
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, estimatedLength);
		if (deadline.before(cal)) {
            System.out.println("This is false");
			return false;
		}

		byte[] bytes1 = null;
		byte[] bytes2 = null;
		try {
            bytes1 = Serializer.serialize(dynamicList);
            bytes2 = Serializer.serialize(deadlineList);
        } catch (Exception e) {
            Log.e("shit1", e.toString());
        }
//        if(dynamicList != null){
//            DynamicEvent last = dynamicList.findLastEvent();
//            if(last != null){
//                Calendar lastDeadline = last.getDeadline();
//                Calendar lastEndTime = last.getEndTime();
//                lastEndTime.add(Calendar.MINUTE, estimatedLength);
//                boolean canInsert = false;
//                if (lastDeadline.before(deadline)) {
//                    canInsert = lastEndTime.before((deadline));
//                } else {
//                    DynamicEvent dynamicEvent = new DynamicEvent(name, isStatic, location, description, color, deadline,estimatedLength, isFinished);
//                    long id = System.currentTimeMillis();
//                    dynamicEvent.setId(id);
//                    dynamicSort(dynamicEvent);
//                    DynamicEvent tempEvent = dynamicList.findLastEventById(id);
//                    Calendar tempEndTime = tempEvent.getEndTime();
//                    canInsert = lastEndTime.before(lastDeadline) &&  tempEndTime.before(deadline);
//                }
//                Log.e("checkLast", ""+canInsert);
//                if(!canInsert){
//                    return false;
//                }
//            }
//        }


//        else if(dynamicList != null) {
//            DynamicEvent last = dynamicList.findLastEvent();
//            if(last != null) {
//                Calendar cal2 = last.getEndTime();
//                System.out.println("cal2 endtime");
//                cal2.add(Calendar.MINUTE, estimatedLength);
//                if (deadline.before(cal2)) {
//                    System.out.println("This is false 2");
//                    return false;
//                }
//            }
//        }
		Log.e("shit", "whether null: "+deadlineList);
		DynamicEvent dynamicEvent = new DynamicEvent(name, isStatic, location, description, color, deadline,estimatedLength, isFinished);
		dynamicEvent.setId(System.currentTimeMillis());
		deadlineList.addEvent(dynamicEvent);
		check = EventListHandler.dynamicSort(dynamicEvent);
		Log.e("shit", "whether null 1: "+deadlineList);
		Log.e("shit", "check is: " + check);

        if (!check) {
            Object o1 = null;
            Object o2 = null;
            try {
                o1 = Serializer.deserialize(bytes1);
                o2 = Serializer.deserialize(bytes2);
            } catch (Exception e) {
				Log.e("shit2", e.toString());
            }
            dynamicList = (DynamicEventList) o1;
            deadlineList = (DynamicEventList) o2;
        }

		Log.e("shit", "whether null 2: "+deadlineList);

		return check;
	}

	/*checks if there are conflicts in the static time table, if there are conflicts, return the two conflicted
    events as a single event*/
	private static void checkConflict(PriorityQueue<StaticEvent> currStaticEList, PriorityQueue<StaticEvent> sortedStaticEList)
			throws CalendarError {

		StaticEvent firstCheck = null;
		StaticEvent secondCheck = null;
		Calendar newStartTime = null;
		Calendar newEndTime = null;
		while (!currStaticEList.isEmpty()) {

			firstCheck = currStaticEList.poll();


			if (!currStaticEList.isEmpty()) {

				secondCheck = currStaticEList.peek();


				if (firstCheck.getEndTime().compareTo(secondCheck.getStartTime()) >= 0) {
					System.out.println("Conflict");

					if (firstCheck.getStartTime().compareTo(secondCheck.getStartTime()) <= 0){
						newStartTime = firstCheck.getStartTime();
					}
					else {
						newStartTime = secondCheck.getStartTime();
					}
					if (firstCheck.getEndTime().compareTo(secondCheck.getEndTime()) >= 0){
						newEndTime = firstCheck.getEndTime();
					}
					else {
						newEndTime = secondCheck.getEndTime();
					}
					StaticEvent newevent = new StaticEvent(firstCheck.getName(),
							firstCheck.getLocation(), newStartTime, newEndTime,
							firstCheck.isStatic(), firstCheck.isPeriodic(), firstCheck.isFinished(),
							firstCheck.getDescription(), firstCheck.getColor());
					//remove secondCheck
					currStaticEList.poll();
					//add newEvent back to current list to check for next conflict
					currStaticEList.add(newevent);

				}
				else{
					sortedStaticEList.add(firstCheck);
					firstCheck = null;
					secondCheck = null;
				}
			}
			else{
				sortedStaticEList.add(firstCheck);
			}
		}

	}

	//#1 on the order list
	//remove static time that are before and after 9a/p
	public static void purgeStaticList(PriorityQueue<StaticEvent> sortedStaticEList) {
		StaticEvent time;

		Iterator<StaticEvent> it = sortedStaticEList.iterator();

		while(it.hasNext()){
			//if earlier than 9am set to 9am
			time = it.next();
			//if the start and end time are both earlier than 9am
			//System.out.println(time.getEndTime().get(Calendar.HOUR_OF_DAY) - startTimeOfDay);
			if (time.getEndTime().get(Calendar.HOUR_OF_DAY) - startTimeOfDay < 0) {
				it.remove();
				continue;
			}
			//if start is earlier than 9am but end time is later than 9 am
			if (time.getStartTime().get(Calendar.HOUR_OF_DAY) - startTimeOfDay < 0 && time.getEndTime().get(Calendar.HOUR_OF_DAY)- startTimeOfDay >= 0) {
				time.getStartTime().set(Calendar.HOUR_OF_DAY, startTimeOfDay);
				time.getStartTime().set(Calendar.MINUTE,0);
				continue;
			}

			//if the start and end time are both later than 9pm
			if (time.getStartTime().get(Calendar.HOUR_OF_DAY) - endTimeOfDay >= 0) {
				it.remove();
				continue;
			}
			//if start is earlier than 9pm but end time is later than 9pm
			if ((time.getStartTime().get(Calendar.HOUR_OF_DAY) - endTimeOfDay < 0) && (time.getEndTime().get(Calendar.HOUR_OF_DAY) - endTimeOfDay >= 0)) {
				time.getEndTime().set(Calendar.HOUR_OF_DAY, endTimeOfDay);
				time.getEndTime().set(Calendar.MINUTE,0);
				continue;
			}
		}
	}


	private static int daysBetween(Calendar d1, Calendar d2) {

		return (int) (Math.abs(d2.getTime().getTime() - d1.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}

	//#2 on the purge list
	//returns false if no free time, true will write freetime to freeList
	private static boolean updateFreeTime(PriorityQueue<StaticEvent> sortedStaticEList, PriorityQueue<DynamicEvent> currDynamicEList,
			PriorityQueue<StaticEvent> freeList, PriorityQueue<StaticEvent> sortedfreeList) throws CalendarError {

		//get deadline from last of currDynamicEvent
		//		DynamicEvent print = null;
		//		Iterator<DynamicEvent> itdyntest = currDynamicEList.iterator();
		//		while(itdyntest.hasNext()){
		//			print = itdyntest.next();
		//			System.out.println("Day: " +print.getDeadline().get(Calendar.DAY_OF_MONTH));
		//		}

		Iterator<DynamicEvent> itdyn = currDynamicEList.iterator();
		boolean checker = false;
		DynamicEvent tempor = null;
		while(itdyn.hasNext()){
			DynamicEvent dyn = itdyn.next();
			if(checker == false)
				tempor= dyn;
			checker = true;
			if(dyn.getDeadline().getTimeInMillis() - tempor.getDeadline().getTimeInMillis() > 0){
				tempor = dyn;
			}
		}

		DynamicEvent test = tempor;
		Date start = test.getDeadline().getTime();
		DateFormat time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		System.out.println("Latest Deadline: "+time.format(start));


		DynamicEvent lastdynamicevent = tempor;
		Calendar currStartTime = Calendar.getInstance();
		Calendar currEndTime = Calendar.getInstance();

		Calendar lastDynamicTime = lastdynamicevent.getDeadline();



		StaticEvent freeBlock = null;
		int days = EventListHandler.daysBetween(lastDynamicTime, currStartTime) ;
		if(Math.abs(Math.abs(lastDynamicTime.get(Calendar.DAY_OF_MONTH) - currStartTime.get(Calendar.DAY_OF_MONTH))-days) == 1)
			days++;

		System.out.println("Days between: " + days);
		//System.out.println(days);
		boolean check = false;
		for (int i = 0; i <= days; i++) {
			//make freeTime block
			Calendar startTime = Calendar.getInstance();
			Calendar endTime = Calendar.getInstance();
			//System.out.println(startTime.get(Calendar.HOUR_OF_DAY));

			if (i == 0) {
				if(startTime.get(Calendar.HOUR_OF_DAY) - 21 >= 0 && check == false){
					currStartTime.add(Calendar.DAY_OF_MONTH, 1);
					currEndTime.add(Calendar.DAY_OF_MONTH, 1);
					startTime.setTime(currStartTime.getTime());
					startTime.set(Calendar.HOUR_OF_DAY, 9);
					endTime.setTime(currEndTime.getTime());
					endTime.set(Calendar.HOUR_OF_DAY, 21);
					endTime.set(Calendar.MINUTE, 0);
					check = true;
					continue;
				}
				if(startTime.get(Calendar.HOUR_OF_DAY) < 9){
					continue;
				}
				startTime.setTime(currStartTime.getTime());
				endTime.setTime(currEndTime.getTime());
				endTime.set(Calendar.HOUR_OF_DAY, endTimeOfDay);
				endTime.set(Calendar.MINUTE, 0);
			} else if (i == days) {
				startTime.setTime(currStartTime.getTime());
				startTime.set(Calendar.HOUR_OF_DAY, startTimeOfDay);
				startTime.set(Calendar.MINUTE, 0);
				endTime.setTime(lastDynamicTime.getTime());
			} else {
				startTime.setTime(currStartTime.getTime());
				endTime.setTime(currEndTime.getTime());
				startTime.set(Calendar.HOUR_OF_DAY, startTimeOfDay);
				startTime.set(Calendar.MINUTE, 0);
				endTime.set(Calendar.MINUTE, 0);
				endTime.set(Calendar.HOUR_OF_DAY, endTimeOfDay);
			}


			freeBlock = new StaticEvent("free time", "null", startTime, endTime,
					true, false, false, "null", "null");

			freeList.add(freeBlock);

			currStartTime.add(Calendar.DAY_OF_MONTH, 1);
			currEndTime.add(Calendar.DAY_OF_MONTH, 1);
		}





		//init freetime for the peek operation
		Calendar originalEndTime = null;
		StaticEvent freetime2;
		//		StaticEvent se;
		//		Iterator<StaticEvent> it = sortedStaticEList.iterator();
		//		while(it.hasNext()){
		//			se = it.next();
		//			Date start = se.getStartTime().getTime();
		//			Date end = se.getEndTime().getTime();
		//			DateFormat time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		//			System.out.println(time.format(start));
		//			System.out.println(time.format(end));
		//		}

		//Iterator<StaticEvent> it = freeList.iterator();

		//this while loop will get all the free time blocks from available times and store
		//the free times in freeList, loop condition is while the sorted static event list is not empty keep going
		while (!freeList.isEmpty()) {
			StaticEvent freetime = freeList.poll();
			//backup for freetime
			StaticEvent temp2 = freetime;

			if(sortedStaticEList.isEmpty()){
				sortedfreeList.add(freetime);
			}
			else{
				//get temp as a staticEvent from the top of the sorted Q
				StaticEvent temp = sortedStaticEList.peek();
				System.out.println("Temp starttime is: "+temp.getStartTime().get(Calendar.DAY_OF_MONTH)+" " +temp.getStartTime().get(Calendar.HOUR_OF_DAY) + " " + temp.getStartTime().get(Calendar.MINUTE));
				System.out.println("Temp endtime is: "+temp.getEndTime().get(Calendar.DAY_OF_MONTH)+" " +temp.getEndTime().get(Calendar.HOUR_OF_DAY) + " " + temp.getEndTime().get(Calendar.MINUTE));
				System.out.println("Free starttime is: " + freetime.getStartTime().get(Calendar.DAY_OF_MONTH)+" " +freetime.getStartTime().get(Calendar.HOUR_OF_DAY) + " "+freetime.getStartTime().get(Calendar.MINUTE));
				System.out.println("Free endtime is: " + freetime.getEndTime().get(Calendar.DAY_OF_MONTH)+" " +freetime.getEndTime().get(Calendar.HOUR_OF_DAY) + " "+freetime.getEndTime().get(Calendar.MINUTE) );
				//get freetime a staticEvent from the top of the freetime Q



				if(temp.getStartTime().get(Calendar.YEAR) == freetime.getStartTime().get(Calendar.YEAR)
						&& temp.getStartTime().get(Calendar.MONTH) == freetime.getStartTime().get(Calendar.MONTH)
						&& temp.getStartTime().get(Calendar.DAY_OF_MONTH) == freetime.getStartTime().get(Calendar.DAY_OF_MONTH)){


					//if the staticEvent's time is earlier the freeBlock's start, remove staticEvent from Q
					if (freetime.getStartTime().compareTo(temp.getEndTime()) >= 0) {
						//System.out.println("polled1");
						sortedStaticEList.poll();
						freeList.add(temp2);
						continue;
					}

					//if the static events starts before the freetime block but ends after
					else if (freetime.getStartTime().compareTo(temp.getStartTime()) > 0 &&
							(freetime.getStartTime()).compareTo(temp.getEndTime()) < 0) {
						//set the start of the freeTime block to be the end of the static event
						//System.out.println("polled");
						freetime.setStartTime(temp.getEndTime());
						freeList.add(freetime);
						//remove the static event
						sortedStaticEList.poll();
					}

					//when we can actually start planning the freetime. when the static event start after the free time starts
					else if (freetime.getStartTime().compareTo(temp.getStartTime()) < 0) {
						//System.out.println("ran");
						//this case we don't want because the event ends later than our freetime
						if (temp.getEndTime().compareTo(freetime.getEndTime()) > 0) {
							freetime.setEndTime(temp.getStartTime());
							//System.out.println("ran2");
							sortedfreeList.add(freetime);
							sortedStaticEList.poll();
						}
						else{

							//System.out.println("ran3");
							//set start and end for the freetime to add to the Q
							originalEndTime = freetime.getEndTime();
							//							System.out.println("originalEndTime " + originalEndTime.get(Calendar.HOUR_OF_DAY));
							freetime.setEndTime(temp.getStartTime());
							sortedfreeList.add(freetime);
							//							freetime.getStartTime().add(Calendar.MONTH, 1);
							//							System.out.println("free time start " + freetime.getStartTime().get(Calendar.YEAR) + " "
							//									+freetime.getStartTime().get(Calendar.MONTH) + " "
							//									+freetime.getStartTime().get(Calendar.DAY_OF_MONTH) + " "
							//									+freetime.getStartTime().get(Calendar.HOUR_OF_DAY) + " "
							//									+freetime.getStartTime().get(Calendar.MINUTE) + " ");
							//							freetime.getEndTime().add(Calendar.MONTH, 1);
							//							System.out.println("free time end " + freetime.getEndTime().get(Calendar.YEAR) + " "
							//									+freetime.getEndTime().get(Calendar.MONTH) + " "
							//									+freetime.getEndTime().get(Calendar.DAY_OF_MONTH) + " "
							//									+freetime.getEndTime().get(Calendar.HOUR_OF_DAY) + " "
							//									+freetime.getEndTime().get(Calendar.MINUTE) + " ");

							freetime2 =  new StaticEvent("free time", "null", Calendar.getInstance(), Calendar.getInstance(),
									true, false, false, "null", "null");
							freetime2.setStartTime(temp.getEndTime());
							//							System.out.println("MONTH: "+freetime2.getStartTime().get(Calendar.MONTH));
							//System.out.println("freetime2 start " + freetime2.getEndTime().get(Calendar.HOUR_OF_DAY));
							freetime2.setEndTime(originalEndTime);
							//System.out.println("freetime2 end " + freetime2.getEndTime().get(Calendar.HOUR_OF_DAY));

							freeList.add(freetime2);


							//remove the static event to check from the Q
							sortedStaticEList.poll();
						}
					}

					//if the start time of then static event if after the end of the free time block, remove static
					else if (freetime.getEndTime().compareTo(temp.getStartTime()) < 0) {
						sortedStaticEList.poll();
					}
				}

				else if(temp.getStartTime().get(Calendar.YEAR) == freetime.getStartTime().get(Calendar.YEAR)
						&& temp.getStartTime().get(Calendar.MONTH) == freetime.getStartTime().get(Calendar.MONTH)
						&& temp.getStartTime().get(Calendar.DAY_OF_MONTH) - freetime.getStartTime().get(Calendar.DAY_OF_MONTH) > 0){
					sortedfreeList.add(freetime);
				}
				else{
					freeList.add(freetime);
					sortedStaticEList.poll();
				}
			}
		}

		return true;
	}

	//#3 on the order list
	//function to strip freetime of blocks less than 30min, do not remove 10 min for now
	public static void purgefreeTime(PriorityQueue<StaticEvent> sortedfreeList, PriorityQueue<StaticEvent> newsortedfreeList) {
		StaticEvent freetime;

		//		StaticEvent temp;
		//
		//		Iterator<StaticEvent> it = sortedfreeList.iterator();
		//		while(it.hasNext())
		//		{
		//			temp = it.next();
		//						System.out.println("Purge Loop Debug Srt: " + temp.getStartTime().get(Calendar.YEAR)+ " " +temp.getStartTime().get(Calendar.MONTH) + " "+
		//								temp.getStartTime().get(Calendar.DAY_OF_MONTH)+" "+temp.getStartTime().get(Calendar.HOUR_OF_DAY) + " " + temp.getStartTime().get(Calendar.MINUTE));
		//						System.out.println("Purge Loop Debug End: " + temp.getEndTime().get(Calendar.YEAR)+ " "+ temp.getEndTime().get(Calendar.MONTH) + " "+
		//								temp.getEndTime().get(Calendar.DAY_OF_MONTH)+" "+temp.getEndTime().get(Calendar.HOUR_OF_DAY) + " " + temp.getEndTime().get(Calendar.MINUTE));
		//		}

		while(!sortedfreeList.isEmpty()){

			//System.out.println("In purgefreeTime Loop");
			freetime = sortedfreeList.peek();

			//System.out.println((Math.abs(freetime.getEndTime().getTime().getTime() - freetime.getStartTime().getTime().getTime()) / (1000 * 60)));
			if ( 60*(freetime.getEndTime().get(Calendar.HOUR_OF_DAY) - freetime.getStartTime().get(Calendar.HOUR_OF_DAY)) +
					freetime.getEndTime().get(Calendar.MINUTE) - freetime.getStartTime().get(Calendar.MINUTE)  < 30)
			{
				sortedfreeList.poll();
			}
			else
			{
				newsortedfreeList.add(freetime);
				sortedfreeList.poll();
			}
		}


	}


	//this function will dynamically allocate the freetime and dynamic time from the priority queue to put in the
	//dynamicList. Will return true if the allocation time is enough, false if otherwise
	public static boolean dynamicAllocation(PriorityQueue<StaticEvent> sortedfreeList, PriorityQueue<DynamicEvent> currDynamicEList) throws CalendarError {

		//System.out.print("entering function");
		int freehours = countHoursFreeTime(sortedfreeList);
		int dynamichours = countHoursDynamicTime();

		System.out.println("Freehours: " + freehours/60);
		System.out.println("Dynamichours: "+ dynamichours/60);

		if(freehours < dynamichours) {
			System.out.println("Freehours: " + freehours);
			System.out.println("Dynamichours: "+ dynamichours);
			System.out.println("freehours less than dynamichours");
			return false;
        }

		/*
		 * This algorithm will allocate the freetime blocks with the dynamic events
		 * Things to consider:
		 *

		 *
		 * loop condition: while the currDynamicEList is not empty:
		 * extra check to see if the free list is empty or not
		 *
		 * if the dynamicTime estimatedlength < freeTime length : add dynamic event with start time
		 *  set to freetime start, endtime set to freetime start + estimatedlength
		 *  set freeTime startTime to be startTime + estimatedlength, put free time back in sortedfreeList
		 *  purge free time list
		 *  add dynamic event to dynamicList
		 *  pop from currDynamicEList
		 *  continue;
		 *
		 * if the dynamicTime estimatedlength = freeTime length: add dynamic event with start time set to
		 * freetime start, endtime set to freetime end.
		 * add dynamic event to dynamicList
		 * pop both lists
		 * continue;
		 *
		 * if the dynamicTime estimatedlength > freeTime length: add dynamic event with start time set to freetime start,
		 * endtime set to freetime end. pop freetime list.
		 * put dynamic time back in currDynamicElist with estimatedTime - (freetime endtime - freetime starttime)
		 * continue;
		 *
		 */
		StaticEvent freetime = null;
		DynamicEvent dynamic = null;
		DynamicEvent newDE;
		DynamicEvent newDE2;
		StaticEvent freetimetemp = null;

		while(!currDynamicEList.isEmpty()){
			System.out.println("Size is equal to: "+ currDynamicEList.size());
			//			System.out.println("in loop");
			//if the free is list is empty before the dynamicList is empty, return false
			dynamic = currDynamicEList.peek();
			freetime = sortedfreeList.peek();

			if(sortedfreeList.isEmpty()){
				System.out.println("freelist is empty");
				return false;}

			/*if dynamic length is less than free time length*/
			if(dynamic.getUpdatedlength() < ((int)(Math.abs(freetime.getEndTime().getTime().getTime() - freetime.getStartTime().getTime().getTime()) / (1000 * 60)))){
				//System.out.println("updated len < freetime");
				newDE = new DynamicEvent(dynamic.getName(), false, dynamic.getLocation(), dynamic.getDescription(), dynamic.getColor(),
						dynamic.getDeadline(), dynamic.getEstimatedLength(),dynamic.isFinished());
				newDE.setId(dynamic.getId());
				newDE.setStartTime(freetime.getStartTime());
				newDE.getEndTime().set(Calendar.SECOND, 0);
				newDE.getStartTime().set(Calendar.SECOND,0);
				freetimetemp = sortedfreeList.poll();
				(freetimetemp.getStartTime()).add(Calendar.MINUTE, dynamic.getUpdatedlength());

				//System.out.println("freetimetemp: " +freetimetemp.getStartTime().get(Calendar.DAY_OF_MONTH)+" "+ freetimetemp.getStartTime().get(Calendar.HOUR_OF_DAY) + " "+freetimetemp.getStartTime().get(Calendar.MINUTE));
				newDE.setEndTime(freetime.getStartTime());
				sortedfreeList.add(freetimetemp);

				if(((int)(Math.abs(newDE.getEndTime().getTime().getTime() - newDE.getStartTime().getTime().getTime()) / (1000 * 60))) >= 30)
					dynamicList.addEvent(newDE);

				//System.out.println("<:                                   "+newDE.getName() + " "+newDE.getId());
				currDynamicEList.poll();
			}

			else if(dynamic.getUpdatedlength() == ((int)(Math.abs(freetime.getEndTime().getTime().getTime() - freetime.getStartTime().getTime().getTime()) / (1000 * 60)))){
				//System.out.println("updated len == freetime");
				newDE = new DynamicEvent(dynamic.getName(), false, dynamic.getLocation(), dynamic.getDescription(), dynamic.getColor(),
						dynamic.getDeadline(), dynamic.getEstimatedLength(),dynamic.isFinished());
				newDE.setId(dynamic.getId());
				newDE.setStartTime(freetime.getStartTime());
				newDE.setEndTime(freetime.getEndTime());
				newDE.getEndTime().set(Calendar.SECOND, 0);
				newDE.getStartTime().set(Calendar.SECOND, 0);
				if(((int)(Math.abs(newDE.getEndTime().getTime().getTime() - newDE.getStartTime().getTime().getTime()) / (1000 * 60))) >= 30)
					dynamicList.addEvent(newDE);
				currDynamicEList.poll();
				sortedfreeList.poll();
			}

			/*
			 * if the dynamiclength is more than the freetime length.
			 */
			else if(dynamic.getUpdatedlength() > ((int)(Math.abs(freetime.getEndTime().getTime().getTime() - freetime.getStartTime().getTime().getTime()) / (1000 * 60)))) {
                //System.out.println("updated len > freetime");

                newDE = new DynamicEvent(dynamic.getName(), false, dynamic.getLocation(), dynamic.getDescription(), dynamic.getColor(),
                        dynamic.getDeadline(), dynamic.getEstimatedLength(), dynamic.isFinished());
                newDE.setId(dynamic.getId());
                newDE2 = new DynamicEvent(dynamic.getName(), false, dynamic.getLocation(), dynamic.getDescription(), dynamic.getColor(),
                        dynamic.getDeadline(), dynamic.getEstimatedLength(), dynamic.isFinished());
                newDE2.setId(dynamic.getId());

                newDE.setStartTime(freetime.getStartTime());
                newDE.setEndTime(freetime.getEndTime());
				newDE.getEndTime().set(Calendar.SECOND, 0);
				newDE.getStartTime().set(Calendar.SECOND, 0);

                if (((int) (Math.abs(newDE.getEndTime().getTime().getTime() - newDE.getStartTime().getTime().getTime()) / (1000 * 60))) >= 30)
                    dynamicList.addEvent(newDE);

                //System.out.println(">:                                      "+newDE.getName() + " "+newDE.getId());
                sortedfreeList.poll();
                currDynamicEList.poll();

                newDE2.setUpdatedlength(dynamic.getUpdatedlength() - (60 * (freetime.getEndTime().get(Calendar.HOUR_OF_DAY) - freetime.getStartTime().get(Calendar.HOUR_OF_DAY))
                        + (freetime.getEndTime().get(Calendar.MINUTE) - freetime.getStartTime().get(Calendar.MINUTE))));

                currDynamicEList.add(newDE2);

            }

		}

		return true;
	}


	//method to count the number of hours in the free time
	private static int countHoursFreeTime(PriorityQueue<StaticEvent> sortedfreeList){
		int freemins = 0;
		StaticEvent temp = null;
		Iterator<StaticEvent> it = sortedfreeList.iterator();
		while(it.hasNext()){
			temp = it.next();
			freemins += ((temp.getEndTime().getTime().getTime() - temp.getStartTime().getTime().getTime())/(1000*60));
		}
		//System.out.println("Check freemins: " + freemins);
		return freemins;

	}

	//method to count the number of hours in dynamicEList
	private static int countHoursDynamicTime(){
		int mins = 0;
		DynamicEvent temp = null;
		ArrayList<DynamicEvent> dl = deadlineList.getList();
		for (int i =0; i< dl.size(); i++){
			temp = dl.get(i);
			mins += temp.getUpdatedlength();
		}
		return mins;
	}












}
