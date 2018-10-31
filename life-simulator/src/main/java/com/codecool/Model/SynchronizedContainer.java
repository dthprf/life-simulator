package com.codecool.Model;

import com.codecool.Model.MobData.MobData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SynchronizedContainer {

    private List<MobData> mobs;
    private List<Resource> resources;

    public SynchronizedContainer(List<MobData> mobs, List<Resource> resources) {
        this.mobs = mobs;
        this.resources = resources;
    }

    public void addMob(MobData mob) {
        mobs.add(mob);
    }

    public void removeMob(MobData mob) {
        mobs.remove(mob);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public synchronized boolean removeResource(Resource resource) {
        return resources.remove(resource);
    }

    public synchronized Resource removeResourceOfType(String type) {
        for (Resource resource : resources) {
            if (resource.getName().equalsIgnoreCase(type)) {
                resources.remove(resource);
                return resource;
            }
        }
        return null;
    }

    public boolean hasMobs() {
        return !mobs.isEmpty();
    }

    public boolean hasMobsOfType(String breed) {
        for (MobData mobData : this.mobs) {
            if (mobData.getBreed().equals(breed)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasResourceOfType(String name) {
        for (Resource resource : this.resources) {
            if (resource.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<MobData> getMobs() {
        return mobs;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public String asChar() {
        if (mobs.isEmpty() && resources.isEmpty()) {
            return " ";
        }
        if (!mobs.isEmpty()) {
            return String.valueOf(mobs.get(0).getBreed().charAt(0)).toUpperCase();
        } else {
            return String.valueOf(resources.get(0).getName().charAt(0)).toLowerCase();
        }
    }

    public MobData getBestPrey(MobData hunter) {
        MobData prey = null;
        for(MobData mob : mobs) {
            if (mob.getBreed().equalsIgnoreCase(hunter.getBreed())) {
                continue;
            }
            if (prey == null || prey.getEnergy() > mob.getEnergy()) {
                prey = mob;
            }
        }
        return prey;
    }

    public Resource getBestFood(String[] foodList) {
        for(String food : foodList) {
            Resource r = removeResourceOfType(food);
            if (r != null) {
                return r;
            }
        }
        return null;
    }
}
