package kdt.hackathon.applysecurity.repository;

import kdt.hackathon.applysecurity.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberReopository  extends JpaRepository<Member, String> {

}
