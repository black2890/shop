package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 새로운 멤버 저장 비즈니스 로직
    public Member saveMember(Member member){
        validateDuplicateMember(member);    //중복 검사를 거친 다음
        return memberRepository.save(member);   //회원 등록 절차 진행
    }

    // 중복 회원인지 검사 비즈니스 로직
    private void validateDuplicateMember(Member member){
        // 멤버
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
        /*
        Member findMember = memberRepository.findByEmail(member.getEmail()); 동작이
        JPA Persistance Context에서
        멤버 객체의 이메일을 가져와서
        memberRepository.findByEmail을 통해
        동일한 이메일이 있는지 Persistance Context에서 우선적으로 조회하고
        없으면 DB에서 있는지 조회하고
        없다면 findMember에 NULL값을 저장하고
        있다면 findMember에 해당 값을 저장하고
        없을 경우
        if(findMember != null){
                    throw new IllegalStateException("이미 가입된 회원입니다.");
                }코드를 통해
        이미 가입된 회원입니다를 출력
         */
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}