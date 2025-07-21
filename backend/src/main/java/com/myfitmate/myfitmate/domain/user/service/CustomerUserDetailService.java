package com.myfitmate.myfitmate.domain.user.service;

import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.repository.UserRepository;
import com.myfitmate.myfitmate.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomerUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 기본 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // 커스텀 메서드
//    public UserDetails loadUserById(Long userId) {
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }


    public UserDetailsImpl loadUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.isDeleted()) {
            throw new UsernameNotFoundException("User is deleted");
        }

        return new UserDetailsImpl(user);
    }

}
