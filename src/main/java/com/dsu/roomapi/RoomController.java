package com.dsu.roomapi;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final List<Room> rooms = new ArrayList<>(List.of(
            new Room(1L, "Seminar A", 8),
            new Room(2L, "Study Pod", 4),
            new Room(3L, "Rooftop Room", 12)));

    private final AtomicLong nextId = new AtomicLong(4);

    @GetMapping
    public List<Room> list(@RequestParam(required = false) Integer minCapacity,
                           @RequestParam(defaultValue = "") String keyword) {
        return rooms.stream()
                .filter(r -> minCapacity == null || r.capacity() >= minCapacity)
                .filter(r -> r.name().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findOne(@PathVariable Long id) {
        return rooms.stream()
                .filter(r -> r.id().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody RoomCreateRequest request) {
        Room room = new Room(nextId.getAndIncrement(), request.name(), request.capacity());
        rooms.add(room);
        return ResponseEntity
                .created(URI.create("/api/rooms/" + room.id()))
                .body(room);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> replace(@PathVariable Long id,
                                        @RequestBody RoomCreateRequest request) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).id().equals(id)) {
                Room updated = new Room(id, request.name(), request.capacity());
                rooms.set(i, updated);
                return ResponseEntity.ok(updated);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean removed = rooms.removeIf(r -> r.id().equals(id));
        return removed ? ResponseEntity.noContent().build()
                       : ResponseEntity.notFound().build();
    }
}
