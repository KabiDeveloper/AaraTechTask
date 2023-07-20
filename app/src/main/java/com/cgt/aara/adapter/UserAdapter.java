package com.cgt.aara.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cgt.aara.R;
import com.cgt.aara.data.UserDataClass;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserDataClass> userDataClassList;

    public UserAdapter(List<UserDataClass> userDataClassList) {
        this.userDataClassList = userDataClassList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =null;
        try {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        try {


            UserDataClass userDataClass = userDataClassList.get(position);
            holder.textName.setText(userDataClass.getName());
            holder.txtMobileNumber.setText(userDataClass.getMobileNumber());
            holder.txtSalary.setText(userDataClass.getIncomeSalary());
            holder.txtAddress.setText(userDataClass.getAddress());
            // Set other views accordingly
            if (userDataClass.getProfilePic() != null && !userDataClass.getProfilePic().isEmpty()) {

                Bitmap bitmap = BitmapFactory.decodeFile(userDataClass.getProfilePic());
                holder.imgProfilePic.setImageBitmap(bitmap);
            } else {
                // Set a placeholder image if no profile picture is available
                holder.imgProfilePic.setImageResource(R.drawable.ic_profile_pic);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return userDataClassList.size();
    }
    // Generate constructor and necessary methods
    public void updateUserList(List<UserDataClass> userDataClassList) {
        try {
            this.userDataClassList = userDataClassList;
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProfilePic;
        private TextView textName;
        private TextView txtMobileNumber;
        private TextView txtAddress;
        private TextView txtSalary;

        public UserViewHolder(View itemView) {
            super(itemView);
            try {
                imgProfilePic = itemView.findViewById(R.id.profilePicImageView);
                textName = itemView.findViewById(R.id.txt_name);
                txtMobileNumber = itemView.findViewById(R.id.txt_mobile_number);
                txtSalary = itemView.findViewById(R.id.txt_salary);
                txtAddress = itemView.findViewById(R.id.txt_address);
            }
            catch (Exception e){
             e.printStackTrace();
            }

        }
    }

    // Implement necessary methods such as onCreateViewHolder, onBindViewHolder, getItemCount
}
