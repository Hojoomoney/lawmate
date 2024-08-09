package site.lawmate.lawyer.service;

import site.lawmate.lawyer.domain.dto.*;
import site.lawmate.lawyer.domain.model.*;

public class ConvertService {

    private LawyerDto convertToDto(Lawyer lawyer) {
        LawyerDto lawyerDto = new LawyerDto();
        lawyerDto.setId(lawyer.getId());
        lawyerDto.setEmail(lawyer.getEmail());
        lawyerDto.setName(lawyer.getName());
        lawyerDto.setPhone(lawyer.getPhone());
        lawyerDto.setBirth(lawyer.getBirth());
        lawyerDto.setLawyerNo(lawyer.getLawyerNo());
        lawyerDto.setMid(lawyer.getMid());
        lawyerDto.setAuth(lawyer.getAuth());

        if (lawyer.getDetail() != null) {
//            lawyerDto.setDetail(convertToDetailDto(lawyer.getDetail()));
        }

        return lawyerDto;
    }

    private Lawyer convertToEntity(LawyerDto lawyerDto) {
        Lawyer lawyer = new Lawyer();
        lawyer.setId(lawyerDto.getId());
        lawyer.setEmail(lawyerDto.getEmail());
        lawyer.setName(lawyerDto.getName());
        lawyer.setPhone(lawyerDto.getPhone());
        lawyer.setBirth(lawyerDto.getBirth());
        lawyer.setLawyerNo(lawyerDto.getLawyerNo());
        lawyer.setMid(lawyerDto.getMid());
        lawyer.setAuth(lawyerDto.getAuth());

        if (lawyerDto.getDetail() != null) {
//            lawyer.setDetail(convertToEntity(lawyerDto.getDetail()));
        }

        return lawyer;
    }
    private LawyerDetailDto convertToDetailDto(LawyerDetail lawyerDetail) {
        LawyerDetailDto lawyerDetailDto = new LawyerDetailDto();
        lawyerDetailDto.setId(lawyerDetail.getId());
        lawyerDetailDto.setBelong(lawyerDetail.getBelong());
        lawyerDetailDto.setAddress(lawyerDetail.getAddress());
        lawyerDetailDto.setAddressDetail(lawyerDetail.getAddressDetail());
        lawyerDetailDto.setBelongPhone(lawyerDetail.getBelongPhone());
        lawyerDetailDto.setLaw(lawyerDetail.getLaw());
        lawyerDetailDto.setVisitCost(lawyerDetail.getVisitCost());
        lawyerDetailDto.setPhoneCost(lawyerDetail.getPhoneCost());
        lawyerDetailDto.setVideoCost(lawyerDetail.getVideoCost());
        lawyerDetailDto.setUniversity(lawyerDetail.getUniversity());
        lawyerDetailDto.setMajor(lawyerDetail.getMajor());
        lawyerDetailDto.setTime(lawyerDetail.getTime());
        lawyerDetailDto.setPremium(lawyerDetail.getPremium());

        return lawyerDetailDto;
    }

    private LawyerDetail convertToEntity(LawyerDetailDto lawyerDetailDto) {
        LawyerDetail lawyerDetail = new LawyerDetail();
        lawyerDetail.setId(lawyerDetailDto.getId());
        lawyerDetail.setBelong(lawyerDetailDto.getBelong());
        lawyerDetail.setAddress(lawyerDetailDto.getAddress());
        lawyerDetail.setAddressDetail(lawyerDetailDto.getAddressDetail());
        lawyerDetail.setBelongPhone(lawyerDetailDto.getBelongPhone());
        lawyerDetail.setLaw(lawyerDetailDto.getLaw());
        lawyerDetail.setVisitCost(lawyerDetailDto.getVisitCost());
        lawyerDetail.setPhoneCost(lawyerDetailDto.getPhoneCost());
        lawyerDetail.setVideoCost(lawyerDetailDto.getVideoCost());
        lawyerDetail.setUniversity(lawyerDetailDto.getUniversity());
        lawyerDetail.setMajor(lawyerDetailDto.getMajor());
        lawyerDetail.setTime(lawyerDetailDto.getTime());
        lawyerDetail.setPremium(lawyerDetailDto.getPremium());

        return lawyerDetail;
    }

    private FileDto convertToFileDto(File file) {
        FileDto fileDto = new FileDto();
        fileDto.setId(file.getId());
        fileDto.setFilename(file.getFilename());
        fileDto.setContentType(file.getContentType());
        fileDto.setUrl(file.getUrl());
        fileDto.setLawyerId(file.getLawyerId());
        fileDto.setPostId(file.getPostId());
        return fileDto;
    }

    private File convertToFileEntity(FileDto fileDto) {
        File file = new File();
        file.setId(fileDto.getId());
        file.setFilename(fileDto.getFilename());
        file.setContentType(fileDto.getContentType());
        file.setUrl(fileDto.getUrl());
        file.setLawyerId(fileDto.getLawyerId());
        file.setPostId(fileDto.getPostId());
        return file;
    }

    private PostDto convertToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setCategory(post.getCategory());
        postDto.setLawyerId(post.getLawyerId());
        postDto.setFileUrls(post.getFileUrls());
        return postDto;
    }

    private Post convertToPostEntity(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCategory(postDto.getCategory());
        post.setLawyerId(postDto.getLawyerId());
        post.setFileUrls(postDto.getFileUrls());
        return post;
    }

    private ReplyDto convertToReplyDto(Reply reply) {
        ReplyDto replyDto = new ReplyDto();
        replyDto.setId(reply.getId());
        replyDto.setContent(reply.getContent());
        replyDto.setArticleId(reply.getArticleId());
        replyDto.setLawyerId(reply.getLawyerId());
        return replyDto;
    }

    private Reply convertToReplyEntity(ReplyDto replyDto) {
        Reply reply = new Reply();
        reply.setId(replyDto.getId());
        reply.setContent(replyDto.getContent());
        reply.setArticleId(replyDto.getArticleId());
        reply.setLawyerId(replyDto.getLawyerId());
        return reply;
    }


}
