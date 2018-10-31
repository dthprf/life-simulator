package com.codecool.Model;

import com.codecool.Model.MobData.MobData;

import java.util.List;

class SynchronizedContainer {

    private List<MobData> mobs;
    private List<Resource> resources;

    SynchronizedContainer(List<MobData> mobs, List<Resource> resources) {
        this.mobs = mobs;
        this.resources = resources;
    }

    void addMob(MobData mob) {
        mobs.add(mob);
    }

    void removeMob(MobData mob) {
        mobs.remove(mob);
    }

    void addResource(Resource resource) {
        resources.add(resource);
    }

    synchronized boolean removeResource(Resource resource) {
        return resources.remove(resource);
    }

    synchronized Resource removeResourceOfType(String type) {
        for (Resource resource : resources) {
            if (resource.getName().equalsIgnoreCase(type)) {
                resources.remove(resource);
                return resource;
            }
        }
        return null;
    }

    boolean hasMobs() {
        return !mobs.isEmpty();
    }

    boolean hasMobsOfType(String breed) {
        for (MobData mobData : this.mobs) {
            if (mobData.getBreed().equals(breed)) {
                return true;
            }
        }
        return false;
    }

    boolean hasResourceOfType(String name) {
        for (Resource resource : this.resources) {
            if (resource.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    List<MobData> getMobs() {
        return mobs;
    }

    List<Resource> getResources() {
        return resources;
    }

    String asChar() {
        if (mobs.isEmpty() && resources.isEmpty()) {
            return " ";
        }
        if (!mobs.isEmpty()) {
            return String.valueOf(mobs.get(0).getAsText());
        } else {
            return String.valueOf(resources.get(0).getAsText());
        }
    }

    MobData getBestPrey(MobData hunter) {
        MobData prey = null;
        for (MobData mob : mobs) {
            if (mob.getBreed().equalsIgnoreCase(hunter.getBreed())) {
                continue;
            }
            if (prey == null || prey.getEnergy() > mob.getEnergy()) {
                prey = mob;
            }
        }
        return prey;
    }

    Resource getBestFood(String[] foodList) {
        for (String food : foodList) {
            Resource r = resources.stream()
                    .filter(resource -> resource.getName().equalsIgnoreCase(food))
                    .findFirst().orElse(null);
            if (r != null) {
                return r;
            }
        }
        return null;
    }
}
