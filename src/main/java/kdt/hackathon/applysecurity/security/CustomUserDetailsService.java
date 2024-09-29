package kdt.hackathon.applysecurity.security;

import kdt.hackathon.applysecurity.entity.User;
import kdt.hackathon.applysecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//  UserDetailsService 인터페이스를 구현하여 데이터베이스에서 User 엔티티를 조회하고
//  CustomUserDetails로 감싸서 반환
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    } // 생성자라서 어노테이션 붙이면 지워도 ㄱㅊ

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(user);
    }
}